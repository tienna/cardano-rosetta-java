package org.cardanofoundation.rosetta.consumer.unit.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import org.cardanofoundation.rosetta.common.entity.Block;
import org.cardanofoundation.rosetta.common.entity.Epoch;
import org.cardanofoundation.rosetta.common.ledgersync.Era;
import org.cardanofoundation.rosetta.consumer.aggregate.AggregatedBlock;
import org.cardanofoundation.rosetta.consumer.aggregate.AggregatedTx;
import org.cardanofoundation.rosetta.consumer.repository.EpochRepository;
import org.cardanofoundation.rosetta.consumer.repository.TxRepository;
import org.cardanofoundation.rosetta.consumer.service.impl.EpochServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class EpochServiceImplTest {

  @Test
  void handleEpoch() {
    EpochRepository epochRepository = Mockito.mock(EpochRepository.class);
    TxRepository txRepository = Mockito.mock(TxRepository.class);
    AggregatedBlock aggregatedBlock = Mockito.mock(AggregatedBlock.class);
    Epoch epoch = new Epoch();
    Era epochEra = Era.BYRON;
    AggregatedTx aggregatedTx1 = Mockito.mock(AggregatedTx.class);
    Mockito.when(aggregatedTx1.getFee()).thenReturn(BigInteger.valueOf(111));
    Mockito.when(aggregatedTx1.getOutSum()).thenReturn(BigInteger.valueOf(10));
    List<AggregatedTx> aggregatedTxList = Arrays.asList(aggregatedTx1);

    Mockito.when(aggregatedBlock.getEpochNo()).thenReturn(32231);
    Mockito.when(aggregatedBlock.getEra()).thenReturn(epochEra);
    Mockito.when(aggregatedBlock.getTxList()).thenReturn(aggregatedTxList);

    epoch.setNo(32231);
    epoch.setBlkCount(32231);
    epoch.setFees(BigInteger.valueOf(1000));
    epoch.setOutSum(BigInteger.valueOf(10));
    epoch.setTxCount(390);
    Optional<Epoch> epochOptional = Optional.of(epoch);
    Mockito.when(epochRepository.findEpochByNo(32231)).thenReturn(epochOptional);

    EpochServiceImpl epochService = new EpochServiceImpl(epochRepository, txRepository);

    assertEquals(BigInteger.valueOf(1000), epoch.getFees());
    assertEquals(BigInteger.valueOf(10), epoch.getOutSum());
    assertEquals(32231, epoch.getBlkCount());
    assertEquals(390, epoch.getTxCount());
    epochService.handleEpoch(List.of(aggregatedBlock));
    assertEquals(391, epoch.getTxCount());
    assertEquals(32232, epoch.getBlkCount());
    assertEquals(BigInteger.valueOf(20), epoch.getOutSum());
    assertEquals(BigInteger.valueOf(1111), epoch.getFees());
    Mockito.verify(epochRepository,Mockito.never()).save(Mockito.any());
  }

  @Test
  void handleEpochNotPresent() {
    EpochRepository epochRepository = Mockito.mock(EpochRepository.class);
    TxRepository txRepository = Mockito.mock(TxRepository.class);
    AggregatedBlock aggregatedBlock = Mockito.mock(AggregatedBlock.class);
    Era epochEra = Era.BYRON;

    Mockito.when(aggregatedBlock.getEpochNo()).thenReturn(32231);
    Mockito.when(aggregatedBlock.getEra()).thenReturn(epochEra);
    Mockito.when(aggregatedBlock.getTxList()).thenReturn(null);

    EpochServiceImpl epochService = new EpochServiceImpl(epochRepository, txRepository);

    epochService.handleEpoch(List.of(aggregatedBlock));
    Mockito.verify(epochRepository,Mockito.times(1)).findEpochByNo(32231);
    Mockito.verify(epochRepository,Mockito.times(1)).saveAll(ArgumentMatchers.anyIterable());

  }

  @Test
  void rollbackEpochStatsDeleteEpoch() {
    EpochRepository epochRepository = Mockito.mock(EpochRepository.class);
    TxRepository txRepository = Mockito.mock(TxRepository.class);
    EpochServiceImpl epochService = new EpochServiceImpl(epochRepository, txRepository);
    Epoch epoch = Mockito.mock(Epoch.class);

    List<Epoch> listEpoch = Arrays.asList(epoch);

    Block block1 = Mockito.mock(Block.class);
    List<Block> rollbackBlocks = Arrays.asList(block1);

    Mockito.when(epoch.getNo()).thenReturn(1);
    Mockito.when(epoch.getOutSum()).thenReturn(BigInteger.valueOf(3));
    Mockito.when(epoch.getTxCount()).thenReturn(2);
    Mockito.when(epoch.getFees()).thenReturn(BigInteger.valueOf(6));
    Mockito.when(epochRepository.findAllByNoIn(Mockito.any())).thenReturn(listEpoch);
    Mockito.when(block1.getEpochNo()).thenReturn(1);

    epochService.rollbackEpochStats(rollbackBlocks);
    Mockito.verify(epochRepository,Mockito.times(1)).delete(epoch);
  }

  @Test
  void rollbackEpochStatsSaveEpoch() {
    EpochRepository epochRepository = Mockito.mock(EpochRepository.class);
    TxRepository txRepository = Mockito.mock(TxRepository.class);
    EpochServiceImpl epochService = new EpochServiceImpl(epochRepository, txRepository);
    Epoch epoch = Mockito.mock(Epoch.class);

    List<Epoch> listEpoch = Arrays.asList(epoch);

    Block block1 = Mockito.mock(Block.class);
    List<Block> rollbackBlocks = Arrays.asList(block1);

    Mockito.when(epoch.getNo()).thenReturn(1);
    Mockito.when(epoch.getOutSum()).thenReturn(BigInteger.valueOf(3));
    Mockito.when(epoch.getTxCount()).thenReturn(2);
    Mockito.when(epoch.getFees()).thenReturn(BigInteger.valueOf(6));
    Mockito.when(epoch.getBlkCount()).thenReturn(2881);
    Mockito.when(epochRepository.findAllByNoIn(Mockito.any())).thenReturn(listEpoch);
    Mockito.when(block1.getEpochNo()).thenReturn(1);

    epochService.rollbackEpochStats(rollbackBlocks);
    Mockito.verify(epochRepository,Mockito.times(1)).save(epoch);
  }
}