package org.cardanofoundation.rosetta.common.cache;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.interceptor.SimpleKey;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.cardanofoundation.rosetta.api.block.model.domain.ProtocolParams;
import org.cardanofoundation.rosetta.api.block.model.entity.LocalProtocolParamsEntity;
import org.cardanofoundation.rosetta.api.block.model.entity.ProtocolParamsEntity;
import org.cardanofoundation.rosetta.api.block.model.repository.EpochParamRepository;
import org.cardanofoundation.rosetta.api.block.model.repository.LocalProtocolParamsRepository;
import org.cardanofoundation.rosetta.common.mapper.ProtocolParamsMapper;
import org.cardanofoundation.rosetta.common.services.ProtocolParamServiceImpl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
@EnableCaching
class ProtocolParamsCacheTest {

  private final static String PROTOCOL_PARAMS_CACHE = "protocolParamsCache";

  @MockitoBean
  private EpochParamRepository epochParamRepository;

  @MockitoBean
  private LocalProtocolParamsRepository localProtocolParamsRepository;

  @MockitoBean
  private ProtocolParamsMapper protocolParamsToEntity;

  @Autowired
  private ProtocolParamServiceImpl genesisService;

  @Autowired
  private CacheManager cacheManager;

  @BeforeEach
  @SuppressWarnings("java:S5786")
  public void setUp() {
    Cache cache = cacheManager.getCache(PROTOCOL_PARAMS_CACHE);
    if (cache != null) {
      cache.clear();
    }
  }

  @Test
  void protocolParamsCacheTest() {
    ProtocolParamsEntity paramsEntity = new ProtocolParamsEntity();
    paramsEntity.setMinFeeA(1);
    ProtocolParams protocolParams = new ProtocolParams();
    protocolParams.setMinFeeA(1);
    LocalProtocolParamsEntity localProtocolParamsEntity = LocalProtocolParamsEntity.builder().protocolParams(protocolParams).build();
    when(localProtocolParamsRepository.getLocalProtocolParams()).thenReturn(Optional.of(localProtocolParamsEntity));

    ProtocolParams result1 = genesisService.findProtocolParameters();
    ProtocolParams result2 = genesisService.findProtocolParameters();

    // Assert that the repository & mapper method is only called once
    verify(localProtocolParamsRepository, times(1)).getLocalProtocolParams();
    assertEquals(result1, result2);

    Cache cache = cacheManager.getCache(PROTOCOL_PARAMS_CACHE);
    assertNotNull(cache);

    Cache.ValueWrapper cachedValue = cache.get(new SimpleKey());
    assertNotNull(cachedValue);

    ProtocolParams cachedProtocolParams = (ProtocolParams) cachedValue.get();
    assertNotNull(cachedProtocolParams);
    assertEquals(protocolParams, cachedProtocolParams);
  }

  @Test
  void protocolParamsCacheEvictTest() {
    ProtocolParamsEntity paramsEntity = new ProtocolParamsEntity();
    paramsEntity.setMinFeeA(1);
    ProtocolParams protocolParams = new ProtocolParams();
    protocolParams.setMinFeeA(1);

    when(epochParamRepository.findLatestProtocolParams()).thenReturn(paramsEntity);
    when(protocolParamsToEntity.mapProtocolParamsToEntity(paramsEntity)).thenReturn(protocolParams);

    ProtocolParams result = genesisService.findProtocolParameters();

    Cache cache = cacheManager.getCache(PROTOCOL_PARAMS_CACHE);
    assertNotNull(cache);
    Cache.ValueWrapper cachedValue = cache.get(new SimpleKey());
    assertNotNull(cachedValue);
    assertEquals(result, cachedValue.get());

    genesisService.evictAllCacheValues();
    assertNull(cacheManager.getCache(PROTOCOL_PARAMS_CACHE).get(new SimpleKey()));
  }

}
