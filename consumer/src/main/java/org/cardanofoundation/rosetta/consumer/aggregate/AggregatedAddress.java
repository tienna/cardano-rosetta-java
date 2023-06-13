package org.cardanofoundation.rosetta.consumer.aggregate;

import com.bloxbean.cardano.client.crypto.Base58;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import org.cardanofoundation.rosetta.common.ledgersync.address.ShelleyAddress;
import org.cardanofoundation.rosetta.common.util.HexUtil;
import org.cardanofoundation.rosetta.consumer.constant.ConsumerConstant;

import java.util.Arrays;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AggregatedAddress {
  String address;
  byte[] addressRaw;
  byte[] stakeReference;
  String paymentCred;
  boolean addressHasScript;

  public static AggregatedAddress from(String address) {
    if (address.startsWith(ConsumerConstant.ADDR_PREFIX)) {
      ShelleyAddress shelleyAddress = new ShelleyAddress(address);
      byte[] stakeReference = new byte[0];
      if (shelleyAddress.containStakeAddress()) {
        stakeReference = shelleyAddress.getStakeReference();
      }

      return new AggregatedAddress(
          address,
          shelleyAddress.getBytes(),
          stakeReference,
          shelleyAddress.getHexPaymentPart(),
          shelleyAddress.addressHasScript()
      );
    }

    return new AggregatedAddress(address,
        Base58.decode(address),
        new byte[0],
        null,
        false
    );
  }

  public boolean hasStakeReference() {
    return !Arrays.equals(stakeReference, new byte[0]);
  }

  public String getStakeAddress() {
    return !hasStakeReference() ? null
        : HexUtil.encodeHexString(stakeReference);
  }
}
