package org.cardanofoundation.rosetta.consumer.service.impl.nativescript;


import com.bloxbean.cardano.client.exception.CborSerializationException;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.cardanofoundation.rosetta.common.entity.Script;
import org.cardanofoundation.rosetta.common.entity.Tx;
import org.cardanofoundation.rosetta.common.enumeration.ScriptType;
import org.cardanofoundation.rosetta.common.ledgersync.nativescript.ScriptAtLeast;
import org.cardanofoundation.rosetta.common.util.HexUtil;
import org.cardanofoundation.rosetta.common.util.JsonUtil;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class ScriptAtLeastServiceImpl extends NativeScriptService<ScriptAtLeast> {

  @Override
  public Script handle(ScriptAtLeast nativeScript, Tx tx) {
    try {
      return buildScript(ScriptType.MULTISIG, HexUtil.encodeHexString(nativeScript.getScriptHash()),tx,
          JsonUtil.getPrettyJson(nativeScript));
    } catch (CborSerializationException e) {
      log.error("Serialize native script hash error, tx {}",tx.getHash());
    }
    return null;
  }


}
