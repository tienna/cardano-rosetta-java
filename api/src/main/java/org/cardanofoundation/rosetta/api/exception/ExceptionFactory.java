package org.cardanofoundation.rosetta.api.exception;


import static org.cardanofoundation.rosetta.api.util.RosettaConstants.RosettaErrorType;


public class ExceptionFactory {

  public static ApiException blockNotFoundException() {
    return new ApiException(RosettaErrorType.BLOCK_NOT_FOUND.toRosettaError(false));
  }

  public static ApiException genesisBlockNotFound() {
    return new ApiException(RosettaErrorType.GENESIS_BLOCK_NOT_FOUND.toRosettaError(false));
  }
  public static ServerException configNotFoundException(){
    return new ServerException("Environment configurations needed to run server were not found");
  }

  public static ApiException invalidBlockChainError() {
    return new ApiException(RosettaErrorType.INVALID_BLOCKCHAIN.toRosettaError(false));
  }

  public static ApiException networkNotFoundError() {
    return new ApiException(RosettaErrorType.NETWORKS_NOT_FOUND.toRosettaError(false));
  }
}
