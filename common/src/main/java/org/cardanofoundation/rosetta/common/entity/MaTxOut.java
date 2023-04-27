package org.cardanofoundation.rosetta.common.entity;

import lombok.*;
import lombok.experimental.SuperBuilder;
import org.cardanofoundation.rosetta.common.validation.Word64Type;
import org.hibernate.Hibernate;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import javax.validation.constraints.Digits;
import java.math.BigInteger;
import java.util.Objects;

@Entity
@Table(name = "ma_tx_out", uniqueConstraints = {
    @UniqueConstraint(name = "unique_ma_tx_out",
        columnNames = {"ident", "tx_out_id"})
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder(toBuilder = true)
public class MaTxOut extends BaseEntity {

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @OnDelete(action = OnDeleteAction.CASCADE)
  @JoinColumn(name = "ident", nullable = false,
      foreignKey = @ForeignKey(name = "ma_tx_out_ident_fkey"))
  @EqualsAndHashCode.Exclude
  private MultiAsset ident;

  @Column(name = "ident", updatable = false, insertable = false)
  private Long identId;

  @Column(name = "quantity", nullable = false, precision = 20)
  @Word64Type
  @Digits(integer = 20, fraction = 0)
  private BigInteger quantity;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @OnDelete(action = OnDeleteAction.CASCADE)
  @JoinColumn(name = "tx_out_id", nullable = false,
      foreignKey = @ForeignKey(name = "ma_tx_out_tx_out_id_fkey"))
  @EqualsAndHashCode.Exclude
  private TxOut txOut;

  @Column(name = "tx_out_id", insertable = false, updatable = false)
  private Long txOutId;

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) {
      return false;
    }
    MaTxOut maTxOut = (MaTxOut) o;
    return id != null && Objects.equals(id, maTxOut.id);
  }

  @Override
  public int hashCode() {
    return getClass().hashCode();
  }
}
