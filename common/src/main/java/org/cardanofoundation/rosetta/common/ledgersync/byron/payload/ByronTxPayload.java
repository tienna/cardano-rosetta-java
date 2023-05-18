package org.cardanofoundation.rosetta.common.ledgersync.byron.payload;

import org.cardanofoundation.rosetta.common.ledgersync.byron.ByronTx;
import org.cardanofoundation.rosetta.common.ledgersync.byron.ByronTxWitnesses;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@EqualsAndHashCode
@Builder
public class ByronTxPayload {

  private ByronTx transaction;
  private List<ByronTxWitnesses> witnesses;
}
