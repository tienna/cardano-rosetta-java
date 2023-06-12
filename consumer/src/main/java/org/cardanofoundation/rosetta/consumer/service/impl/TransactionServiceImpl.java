package org.cardanofoundation.rosetta.consumer.service.impl;

import com.bloxbean.cardano.client.transaction.spec.RedeemerTag;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.cardanofoundation.rosetta.common.entity.*;
import org.cardanofoundation.rosetta.common.ledgersync.certs.Certificate;
import org.cardanofoundation.rosetta.consumer.aggregate.AggregatedBlock;
import org.cardanofoundation.rosetta.consumer.aggregate.AggregatedTx;
import org.cardanofoundation.rosetta.consumer.aggregate.AggregatedTxIn;
import org.cardanofoundation.rosetta.consumer.aggregate.AggregatedTxOut;
import org.cardanofoundation.rosetta.consumer.dto.RedeemerReference;
import org.cardanofoundation.rosetta.consumer.factory.CertificateSyncServiceFactory;
import org.cardanofoundation.rosetta.consumer.repository.ExtraKeyWitnessRepository;
import org.cardanofoundation.rosetta.consumer.repository.TxRepository;
import org.cardanofoundation.rosetta.consumer.service.*;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;


@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@Service
public class TransactionServiceImpl implements TransactionService {

  TxRepository txRepository;
  ExtraKeyWitnessRepository extraKeyWitnessRepository;

  MultiAssetService multiAssetService;
  StakeAddressService stakeAddressService;
  ParamProposalService paramProposalService;
  AddressBalanceService addressBalanceService;
  WithdrawalsService withdrawalsService;
  TxMetaDataService txMetaDataService;
  RedeemerService redeemerService;
  ScriptService scriptService;
  DatumService datumService;

  BlockDataService blockDataService;
  TxOutService txOutService;
  TxInService txInService;

  ReferenceInputService referenceInputService;

  CertificateSyncServiceFactory certificateSyncServiceFactory;
  BatchCertificateDataService batchCertificateDataService;

  @Override
  public void prepareAndHandleTxs(Map<String, Block> blockMap,
                                  Collection<AggregatedBlock> aggregatedBlocks) {
    List<AggregatedTx> aggregatedTxList = aggregatedBlocks.stream()
        .map(AggregatedBlock::getTxList)
        .flatMap(Collection::stream)
        .toList();
    Collection<AggregatedTx> successTxs = new ConcurrentLinkedQueue<>();
    Collection<AggregatedTx> failedTxs = new ConcurrentLinkedQueue<>();

    if (CollectionUtils.isEmpty(aggregatedTxList)) {
      return;
    }

    /*
     * For each aggregated tx, map it to a new tx entity, and check if the currently
     * processing aggregated tx's validity and push it to either a queue of success
     * txs or failed txs
     */
    var txMap = aggregatedTxList.stream().map(aggregatedTx -> {
      Tx tx = new Tx();
      tx.setHash(aggregatedTx.getHash());
      tx.setBlock(blockMap.get(aggregatedTx.getBlockHash()));
      tx.setBlockIndex(aggregatedTx.getBlockIndex());
      tx.setOutSum(aggregatedTx.getOutSum());
      tx.setFee(aggregatedTx.getFee());
      tx.setValidContract(aggregatedTx.isValidContract());
      tx.setDeposit(aggregatedTx.getDeposit());

      if (aggregatedTx.isValidContract()) {
        successTxs.add(aggregatedTx);
      } else {
        failedTxs.add(aggregatedTx);
      }
      return tx;
    }).collect(Collectors.toConcurrentMap(Tx::getHash, Function.identity()));

    // Transaction records need to be saved in sequential order to ease out future queries
    txRepository.saveAll(txMap.values()
        .stream()
        .sorted((tx1, tx2) -> {
          Long tx1BlockNo = tx1.getBlock().getBlockNo();
          Long tx2BlockNo = tx2.getBlock().getBlockNo();
          Long tx1BlockIndex = tx1.getBlockIndex();
          Long tx2BlockIndex = tx2.getBlockIndex();

          if (Objects.equals(tx1BlockNo, tx2BlockNo)) {
            return tx1BlockIndex.compareTo(tx2BlockIndex);
          }

          return tx1BlockNo.compareTo(tx2BlockNo);
        })
        .toList());

    scriptService.handleScripts(aggregatedTxList, txMap);
    datumService.handleDatum(aggregatedTxList, txMap);
    handleTxs(successTxs, failedTxs, txMap);
  }

  private void handleTxs(Collection<AggregatedTx> successTxs,
                         Collection<AggregatedTx> failedTxs, Map<String, Tx> txMap) {

    // Handle extra key witnesses from required signers
    handleExtraKeyWitnesses(successTxs, failedTxs, txMap);

    // Handle Tx contents
    handleTxContents(successTxs, failedTxs, txMap);
  }

  private void handleTxContents(Collection<AggregatedTx> successTxs,
                                Collection<AggregatedTx> failedTxs, Map<String, Tx> txMap) {
    if (CollectionUtils.isEmpty(successTxs) && CollectionUtils.isEmpty(failedTxs)) {
      return;
    }

    // MUST SET FIRST
    // multi asset mint
    long startTime = System.currentTimeMillis();
    multiAssetService.handleMultiAssetMint(successTxs, txMap);

    // Handle stake address and its first appeared tx
    Map<String, StakeAddress> stakeAddressMap = stakeAddressService
        .handleStakeAddressesFromTxs(blockDataService.getStakeAddressTxHashMap(), txMap);

    // tx out
    Collection<TxOut> txOutCollection =
        txOutService.prepareTxOuts(buildAggregatedTxOutMap(successTxs), txMap, stakeAddressMap);

    // handle collateral out as tx out for failed txs
    if (!CollectionUtils.isEmpty(failedTxs)) {
      txOutCollection.addAll(txOutService.prepareTxOuts(
          buildCollateralTxOutMap(failedTxs), txMap, stakeAddressMap));
    }

    // Create an uncommitted tx out map to allow other methods to use
    Map<Pair<String, Short>, TxOut> newTxOutMap = txOutCollection.stream()
        .collect(Collectors.toMap(
            txOut -> Pair.of(txOut.getTx().getHash(), txOut.getIndex()),
            Function.identity()
        ));

    // redeemer
    Map<RedeemerReference<?>, Redeemer> redeemersMap =
        redeemerService.handleRedeemers(successTxs, txMap, newTxOutMap);

    // tx in
    txInService.handleTxIns(successTxs,
        buildTxInsMap(successTxs), txMap, newTxOutMap, redeemersMap);

    // handle collateral input as tx in
    txInService.handleTxIns(failedTxs,
        buildCollateralTxInsMap(failedTxs), txMap, newTxOutMap,
        Collections.emptyMap());

    // auxiliary
    txMetaDataService.handleAuxiliaryDataMaps(txMap);

    //param proposal
    paramProposalService.handleParamProposals(successTxs, txMap);

    // reference inputs
    referenceInputService.handleReferenceInputs(
        buildReferenceTxInsMap(successTxs), txMap, newTxOutMap);

    // certificates
    handleCertificates(successTxs, txMap, redeemersMap, stakeAddressMap);

    // Withdrawals
    withdrawalsService.handleWithdrawal(successTxs, txMap, stakeAddressMap, redeemersMap);

    // Handle address balances
    addressBalanceService.handleAddressBalance(
        blockDataService.getAggregatedAddressBalanceMap(), stakeAddressMap, txMap);
  }

  private Map<String, Set<AggregatedTxIn>> buildTxInsMap(Collection<AggregatedTx> txList) {
    return txList.stream()
        .collect(Collectors.toConcurrentMap(
            AggregatedTx::getHash, AggregatedTx::getTxInputs, (a, b) -> a));
  }

  private Map<String, Set<AggregatedTxIn>> buildCollateralTxInsMap(
      Collection<AggregatedTx> txList) {
    return txList.stream()
        .filter(tx -> !CollectionUtils.isEmpty(tx.getCollateralInputs()))
        .collect(Collectors.toConcurrentMap(
            AggregatedTx::getHash, AggregatedTx::getCollateralInputs, (a, b) -> a));
  }

  private Map<String, Set<AggregatedTxIn>> buildReferenceTxInsMap(
      Collection<AggregatedTx> txList) {
    return txList.stream()
        .filter(tx -> !CollectionUtils.isEmpty(tx.getReferenceInputs()))
        .collect(Collectors.toConcurrentMap(
            AggregatedTx::getHash, AggregatedTx::getReferenceInputs, (a, b) -> a));
  }

  private Map<String, List<AggregatedTxOut>> buildAggregatedTxOutMap(
      Collection<AggregatedTx> txList) {
    return txList.stream()
        .filter(tx -> !CollectionUtils.isEmpty(tx.getTxOutputs()))
        .collect(Collectors.toConcurrentMap(
            AggregatedTx::getHash, AggregatedTx::getTxOutputs, (a, b) -> a));
  }

  private Map<String, List<AggregatedTxOut>> buildCollateralTxOutMap(
      Collection<AggregatedTx> txList) {
    return txList.stream()
        .filter(tx -> Objects.nonNull(tx.getCollateralReturn()))
        .collect(Collectors.toConcurrentMap(
            AggregatedTx::getHash,
            tx -> List.of(tx.getCollateralReturn()),
            (a, b) -> a));
  }

  private void handleCertificates(Collection<AggregatedTx> successTxs, Map<String, Tx> txMap,
                                  Map<RedeemerReference<?>, Redeemer> redeemersMap,
                                  Map<String, StakeAddress> stakeAddressMap) {
    successTxs.forEach(aggregatedTx -> {
      Tx tx = txMap.get(aggregatedTx.getHash());
      if (CollectionUtils.isEmpty(aggregatedTx.getCertificates())) {
        return;
      }

      IntStream.range(0, aggregatedTx.getCertificates().size()).forEach(idx -> {
        Certificate certificate = aggregatedTx.getCertificates().get(idx);

        // Only stake de-registration and stake delegation have redeemers
        RedeemerReference<Certificate> redeemerReference =
            new RedeemerReference<>(RedeemerTag.Cert, certificate);
        Redeemer redeemer = redeemersMap.get(redeemerReference);
        AggregatedBlock aggregatedBlock = blockDataService
            .getAggregatedBlock(aggregatedTx.getBlockHash());
        certificateSyncServiceFactory.handle(
            aggregatedBlock, certificate, idx, tx, redeemer, stakeAddressMap);
      });
    });

    batchCertificateDataService.saveAllAndClearBatchData();
  }

  public void handleExtraKeyWitnesses(Collection<AggregatedTx> successTxs,
                                      Collection<AggregatedTx> failedTxs, Map<String, Tx> txMap) {

    Map<String, Tx> mWitnessTx = new ConcurrentHashMap<>();
    Set<String> hashCollection = new ConcurrentSkipListSet<>();

    /*
     * Map all extra key witnesses hashes to its respective tx and add them to a set
     * which will be used to find all existing hashes from database. The existing hashes
     * will be opted out
     *
     * This process will be done asynchronously
     */
    Stream.concat(successTxs.parallelStream(), failedTxs.parallelStream())
        .filter(aggregatedTx -> !CollectionUtils.isEmpty(aggregatedTx.getRequiredSigners()))
        .forEach(aggregatedTx -> {
          Tx tx = txMap.get(aggregatedTx.getHash());
          aggregatedTx.getRequiredSigners().parallelStream().forEach(hash -> {
            mWitnessTx.put(hash, tx);
            hashCollection.add(hash);
          });
        });

    if (CollectionUtils.isEmpty(hashCollection)) {
      return;
    }

    Set<String> existsWitnessKeys = extraKeyWitnessRepository.findByHashIn(hashCollection);

    // Opt out all existing hashes
    hashCollection.removeAll(existsWitnessKeys);

    // Create new extra key witnesses records
    List<ExtraKeyWitness> extraKeyWitnesses = hashCollection.stream()
        .map(hash -> ExtraKeyWitness.builder()
            .hash(hash)
            .tx(mWitnessTx.get(hash))
            .build())
        .collect(Collectors.toList());

    if (!CollectionUtils.isEmpty(extraKeyWitnesses)) {
      extraKeyWitnessRepository.saveAll(extraKeyWitnesses);
    }
  }
}
