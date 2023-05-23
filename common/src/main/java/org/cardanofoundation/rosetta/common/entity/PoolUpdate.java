package org.cardanofoundation.rosetta.common.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Digits;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.cardanofoundation.rosetta.common.validation.Hash32Type;
import org.cardanofoundation.rosetta.common.validation.Lovelace;
import org.hibernate.Hibernate;

import java.math.BigInteger;
import java.util.Objects;

@Entity
@Table(name = "pool_update", uniqueConstraints = {
    @UniqueConstraint(name = "unique_pool_update",
        columnNames = {"registered_tx_id", "cert_index"})
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder(toBuilder = true)
public class PoolUpdate extends BaseEntity {

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "hash_id", nullable = false,
      foreignKey = @ForeignKey(value = ConstraintMode.NO_CONSTRAINT, name = "none"))
  @EqualsAndHashCode.Exclude
  private PoolHash poolHash;

  @Column(name = "hash_id", updatable = false, insertable = false)
  private Long poolHashId;

  @Column(name = "cert_index", nullable = false)
  private Integer certIndex;

  @Column(name = "vrf_key_hash", nullable = false, length = 64)
  @Hash32Type
  private String vrfKeyHash;

  @Column(name = "pledge", nullable = false, precision = 20)
  @Lovelace
  @Digits(integer = 20, fraction = 0)
  private BigInteger pledge;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "reward_addr_id", nullable = false,
      foreignKey = @ForeignKey(value = ConstraintMode.NO_CONSTRAINT, name = "none"))
  @EqualsAndHashCode.Exclude
  private StakeAddress rewardAddr;

  @Column(name = "reward_addr_id", updatable = false, insertable = false)
  private Long rewardAddrId;

  @Column(name = "active_epoch_no", nullable = false)
  private Integer activeEpochNo;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "meta_id",
      foreignKey = @ForeignKey(value = ConstraintMode.NO_CONSTRAINT, name = "none"))
  @EqualsAndHashCode.Exclude
  private PoolMetadataRef meta;

  @Column(name = "margin", nullable = false)
  private Double margin;

  @Column(name = "fixed_cost", nullable = false, precision = 20)
  @Lovelace
  @Digits(integer = 20, fraction = 0)
  private BigInteger fixedCost;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "registered_tx_id", nullable = false,
      foreignKey = @ForeignKey(value = ConstraintMode.NO_CONSTRAINT, name = "none"))
  @EqualsAndHashCode.Exclude
  private Tx registeredTx;

  @Column(name = "registered_tx_id", updatable = false, insertable = false)
  private Long registeredTxId;

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) {
      return false;
    }
    PoolUpdate that = (PoolUpdate) o;
    return id != null && Objects.equals(id, that.id);
  }

  @Override
  public int hashCode() {
    return getClass().hashCode();
  }
}
