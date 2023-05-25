package org.cardanofoundation.rosetta.api.service.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.cdimascio.dotenv.Dotenv;
import jakarta.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.cardanofoundation.rosetta.api.common.constants.Constants;
import org.cardanofoundation.rosetta.api.config.NetworkConfig;
import org.cardanofoundation.rosetta.api.config.RosettaConfig;
import org.cardanofoundation.rosetta.api.event.AppEvent;
import org.cardanofoundation.rosetta.api.exception.ExceptionFactory;
import org.cardanofoundation.rosetta.api.exception.ServerException;
import org.cardanofoundation.rosetta.api.mapper.DataMapper;
import org.cardanofoundation.rosetta.api.model.Network;
import org.cardanofoundation.rosetta.api.model.NetworkStatus;
import org.cardanofoundation.rosetta.api.model.Peer;
import org.cardanofoundation.rosetta.api.model.Producer;
import org.cardanofoundation.rosetta.api.model.PublicRoot;
import org.cardanofoundation.rosetta.api.model.TopologyConfig;
import org.cardanofoundation.rosetta.api.model.Version;
import org.cardanofoundation.rosetta.api.model.rest.BalanceExemption;
import org.cardanofoundation.rosetta.api.model.rest.MetadataRequest;
import org.cardanofoundation.rosetta.api.model.rest.NetworkListResponse;
import org.cardanofoundation.rosetta.api.model.rest.NetworkOptionsResponse;
import org.cardanofoundation.rosetta.api.model.rest.NetworkRequest;
import org.cardanofoundation.rosetta.api.model.rest.NetworkStatusResponse;
import org.cardanofoundation.rosetta.api.projection.dto.BlockDto;
import org.cardanofoundation.rosetta.api.projection.dto.GenesisBlockDto;
import org.cardanofoundation.rosetta.api.service.BlockService;
import org.cardanofoundation.rosetta.api.service.LedgerDataProviderService;
import org.cardanofoundation.rosetta.api.service.NetworkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

@Service
@Slf4j
public class NetworkServiceImpl implements NetworkService {
  @Autowired
  private RosettaConfig rosettaConfig;

  @Autowired
  private BlockService blockService;

  private static  Dotenv dotenv = Dotenv.load();

  @Autowired
  private LedgerDataProviderService ledgerDataProviderService;


  private List<BalanceExemption> balanceExemptions;

  @PostConstruct
  void loadExemptionsFile() throws IOException {
    if (rosettaConfig.getExemptionsFile() != null) {
      final ObjectMapper objectMapper = new ObjectMapper();
      balanceExemptions = objectMapper.readValue(
          new File(rosettaConfig.getExemptionsFile()),
          new TypeReference<List<BalanceExemption>>() {
          });
    } else {
      balanceExemptions = List.of();
    }
  }

  @Override
  public NetworkListResponse getNetworkList(MetadataRequest metadataRequest) {
    log.info("[networkList] Looking for all supported networks");
    Network supportedNetwork = getSupportedNetwork();
    return DataMapper.mapToNetworkListResponse(supportedNetwork);
  }

  @Override
  public NetworkOptionsResponse getNetworkOptions(NetworkRequest networkRequest) {
    NetworkConfig requestedNetworkConfig = rosettaConfig.networkConfigFromNetworkRequest(networkRequest).orElseThrow();
    NetworkOptionsResponse networkOptionsResponse = new NetworkOptionsResponse();
    log.info("[networkOptions] Looking for networkOptions");


    Version version = new Version();
    version.setRosettaVersion(rosettaConfig.getVersion());
    version.setMiddlewareVersion(rosettaConfig.getImplementationVersion());
    version.setNodeVersion(requestedNetworkConfig.getNodeVersion());
//    networkOptionsResponse.setVersion(version);
//
//        final Allow allow = new Allow();
//        allow.setOperationStatuses(RosettaConstants.ROSETTA_OPERATION_STATUSES);
//        allow.setOperationTypes(RosettaConstants.ROSETTA_OPERATION_TYPES);
//        allow.setErrors(RosettaConstants.ROSETTA_ERRORS);
//        allow.setHistoricalBalanceLookup(true);
//        allow.setCallMethods(List.of());
//        allow.setMempoolCoins(false);
//        allow.setBalanceExemptions(balanceExemptions);
//        networkOptionsResponse.setAllow(allow);
    return networkOptionsResponse;
  }

  @Override
  public NetworkStatusResponse getNetworkStatus(NetworkRequest networkRequest)
      throws ServerException {
    log.debug("[networkStatus] Request received:" + networkRequest.toString());
    log.info("[networkStatus] Looking for latest block");
    NetworkStatus networkStatus = networkStatus();
    return DataMapper.mapToNetworkStatusResponse(networkStatus);
  }

  @Override
  public Network getSupportedNetwork() {
    if(AppEvent.networkId.equals("mainnet")){
      return Network.builder().networkId(AppEvent.networkId).build();
    } else if (AppEvent.networkMagic.equals(BigInteger.valueOf(Constants.PREPROD_NETWORK_MAGIC))) {
      return Network.builder().networkId("preprod").build();
    } else if (AppEvent.networkMagic.equals(BigInteger.valueOf(Constants.PREVIEW_NETWORK_MAGIC))) {
      return Network.builder().networkId("preprod").build();
    }
    return null;
  }

  private NetworkStatus networkStatus() throws ServerException {
    log.info("[networkStatus] Looking for latest block");
    BlockDto latestBlock = ledgerDataProviderService.findLatestBlock();
    log.debug("[networkStatus] Latest block found " + latestBlock);
    log.debug("[networkStatus] Looking for genesis block");
    GenesisBlockDto genesisBlock = ledgerDataProviderService.findGenesisBlock();
    log.debug("[networkStatus] Genesis block found " + genesisBlock);
    return NetworkStatus.builder()
        .latestBlock(latestBlock)
        .genesisBlock(genesisBlock)
        .peers(getPeerFromConfig(readFromFileConfig()))
        .build();
  }

  private List<Peer> getPeerFromConfig(TopologyConfig topologyFile) {
    log.info("[getPeersFromConfig] Looking for peers from topologyFile");
    List<Producer> producers = Optional.ofNullable(topologyFile).map(
            TopologyConfig::getProducers)
        .orElseGet(() -> getPublicRoots(topologyFile.getPublicRoots()));
    log.debug("[getPeersFromConfig] Found " + producers.size() + " peers");
    return producers.stream().map(producer -> new Peer(producer.getAddr())).collect(Collectors.toList());
  }

  private List<Producer> getPublicRoots(List<PublicRoot> publicRoots) {
    if (publicRoots == null) {
      return new ArrayList<>();
    }
    return publicRoots.stream().flatMap(pr -> pr.getAccessPoints().stream())
        .map(ap -> Producer.builder().addr(ap.getAddress()).build())
        .collect(Collectors.toList());

  }

  private TopologyConfig readFromFileConfig() throws ServerException {
    String topologyFilepath = dotenv.get("TOPOLOGY_FILEPATH");
    try {
      ObjectMapper  mapper = new ObjectMapper();
      File file = ResourceUtils.getFile(topologyFilepath);
      return mapper.readValue(file,TopologyConfig.class);

    } catch (IOException e) {
      throw ExceptionFactory.configNotFoundException();
    }
  }
}
