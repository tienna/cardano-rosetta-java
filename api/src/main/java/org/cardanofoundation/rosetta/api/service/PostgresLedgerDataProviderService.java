package org.cardanofoundation.rosetta.api.service;

import static org.cardanofoundation.rosetta.api.util.Formatters.hexFormatter;
import static org.cardanofoundation.rosetta.api.util.Formatters.hexStringToBuffer;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.cardanofoundation.rosetta.api.config.RosettaConfig;
import org.cardanofoundation.rosetta.api.model.rest.BlockIdentifier;
import org.cardanofoundation.rosetta.api.projection.BlockDto;
import org.cardanofoundation.rosetta.api.projection.BlockProjection;
import org.cardanofoundation.rosetta.api.projection.GenesisBlockDto;
import org.cardanofoundation.rosetta.api.projection.GenesisBlockProjection;
import org.cardanofoundation.rosetta.api.repository.BlockRepository;
import org.cardanofoundation.rosetta.api.repository.RewardRepository;
import org.cardanofoundation.rosetta.api.util.Formatters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
@Slf4j
@Component
public class PostgresLedgerDataProviderService implements LedgerDataProviderService {
    @Autowired
    private RosettaConfig rosettaConfig;
    @Autowired
    private BlockRepository blockRepository;
    @Autowired
    private RewardRepository rewardRepository;

    private final Map<String, PostgresLedgerDataProviderClient> clients = new HashMap<>();

    @PostConstruct
    void init() {
        rosettaConfig.getNetworks().forEach(networkConfig -> {
            clients.put(networkConfig.getSanitizedNetworkId(), PostgresLedgerDataProviderClient.builder().networkId(networkConfig.getSanitizedNetworkId()).build());
        });
    }

    @Override
    public BlockIdentifier getTip(final String networkId) {
        if (clients.containsKey(networkId)) {
            return clients.get(networkId).getTip();
        }

        throw new IllegalArgumentException("Invalid network id specified.");
    }

    @Override
    public GenesisBlockDto findGenesisBlock() {
        log.debug("[findGenesisBlock] About to run findGenesisBlock query");
        Page<GenesisBlockProjection> genesisBlockProjectionPage = blockRepository.findGenesisBlock(
                PageRequest.of(0, 1));
        log.debug("[GenesisBlockProjectionPage] is " + genesisBlockProjectionPage);
        genesisBlockProjectionPage.getContent();
        if (!genesisBlockProjectionPage.getContent().isEmpty()) {
            GenesisBlockProjection genesis = genesisBlockProjectionPage
                    .getContent()
                    .get(0);
            return GenesisBlockDto.builder().hash(Formatters.hexFormatter(genesis.getHash()))
                    .number(genesis.getIndex())
                    .build();
        }
        log.debug("[findGenesisBlock] Genesis block was not found");
        return null;
    }

    @Override
    public BlockDto findBlock(Long blockNumber, String blockHash) {
        log.debug("[findBlock] Parameters received for run query blockNumber: " + blockNumber + " , blockHash: " + blockHash);
//        log.info("test block hash value " + ArrayUtils.toObject(hexStringToBuffer(blockHash)));
        byte[] blockHashParam = Objects.nonNull(blockHash) ? hexStringToBuffer(blockHash) : null;
        List<BlockProjection> blockProjections = blockRepository.findBlock(blockNumber, blockHashParam);
        if (blockProjections.size() == 1) {
            log.debug("[findBlock] Block found!");
            BlockProjection blockProjection = blockProjections.get(0);
            return BlockDto.builder()
                    .number(blockProjection.getNumber())
                    .hash(hexFormatter(blockProjection.getHash()))
                    .createdAt(blockProjection.getCreatedAt().getTime())
                    .previousBlockHash(hexFormatter(blockProjection.getPreviousBlockHash()))
                    .previousBlockNumber(blockProjection.getPreviousBlockNumber())
                    .transactionsCount(blockProjection.getTransactionsCount())
                    .createdBy(blockProjection.getCreatedBy())
                    .size(blockProjection.getSize())
                    .epochNo(blockProjection.getEpochNo())
                    .slotNo(blockProjection.getSlotNo())
                    .build();
        }
        log.debug("[findBlock] No block was found");
        return null;
    }

    @Override
    public Double findBalanceByAddressAndBlock(String address, String hash) {
        return rewardRepository.findBalanceByAddressAndBlockSub1(hexStringToBuffer(address), hash)
                - rewardRepository.findBalanceByAddressAndBlockSub2(hexStringToBuffer(address), hash);
    }

    @Override
    public Long findLatestBlockNumber() {
        Page<Long> latestBlockNumberPage = blockRepository.findLatestBlockNumber(
            PageRequest.of(0, 1));
        if (ObjectUtils.isNotEmpty(latestBlockNumberPage.getContent())) {
            return latestBlockNumberPage.getContent().get(0);
        }
        return null;
    }

}
