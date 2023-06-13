package org.cardanofoundation.rosetta.consumer.repository;

import org.cardanofoundation.rosetta.common.entity.Block;
import org.cardanofoundation.rosetta.common.entity.Tx;
import org.cardanofoundation.rosetta.consumer.projection.TxTimeProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Repository
public interface TxRepository extends JpaRepository<Tx, Long> {

  List<Tx> findAllByBlockIn(Collection<Block> blocks);

  Tx findFirstByOrderByIdDesc();

  @Query("SELECT tx.id as txId, "
      + "tx.block.time as txTime, "
      + "(SUM(CASE WHEN txo.addressHasScript = TRUE THEN 1 ELSE 0 END) != 0) AS txWithSc, "
      + "(SUM(CASE WHEN txo.addressHasScript = FALSE AND tm IS NOT NULL THEN 0 ELSE 1 END) = 0) AS txWithMetadataWithoutSc, "
      + "(SUM(CASE WHEN txo.addressHasScript = TRUE OR tm IS NOT NULL THEN 1 ELSE 0 END) = 0) AS simpleTx "
      + "FROM Tx tx "
      + "LEFT JOIN TxIn ti ON ti.txInput = tx "
      + "LEFT JOIN TxOut txo ON (txo.tx = tx) OR (ti.txOut = txo.tx AND ti.txOutIndex = txo.index and txo.addressHasScript = TRUE) "
      + "LEFT JOIN TxMetadata tm ON tm.tx = tx "
      + "WHERE :id IS NULL OR tx.id > :id "
      + "GROUP BY txId, txTime")
  List<TxTimeProjection> findTxWithTimeByIdGreaterThanOrNull(@Param("id") Long id);

  @Query("SELECT tx.id as txId, "
      + "tx.block.time as txTime, "
      + "(SUM(CASE WHEN txo.addressHasScript = TRUE THEN 1 ELSE 0 END) != 0) AS txWithSc, "
      + "(SUM(CASE WHEN txo.addressHasScript = FALSE AND tm IS NOT NULL THEN 0 ELSE 1 END) = 0) AS txWithMetadataWithoutSc, "
      + "(SUM(CASE WHEN txo.addressHasScript = TRUE OR tm IS NOT NULL THEN 1 ELSE 0 END) = 0) AS simpleTx "
      + "FROM Tx tx "
      + "LEFT JOIN TxIn ti ON ti.txInput = tx "
      + "LEFT JOIN TxOut txo ON (txo.tx = tx) OR (ti.txOut = txo.tx AND ti.txOutIndex = txo.index and txo.addressHasScript = TRUE) "
      + "LEFT JOIN TxMetadata tm ON tm.tx = tx "
      + "WHERE tx IN (:txs) "
      + "GROUP BY txId, txTime")
  List<TxTimeProjection> findTxWithTimeByTxIn(@Param("txs") Collection<Tx> txs);
}