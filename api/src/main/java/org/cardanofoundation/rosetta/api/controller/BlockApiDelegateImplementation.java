package org.cardanofoundation.rosetta.api.controller;

import lombok.extern.log4j.Log4j2;
import org.cardanofoundation.rosetta.api.model.rest.BlockRequest;
import org.cardanofoundation.rosetta.api.model.rest.BlockResponse;
import org.cardanofoundation.rosetta.api.model.rest.BlockTransactionRequest;
import org.cardanofoundation.rosetta.api.model.rest.BlockTransactionResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@Log4j2
public class BlockApiDelegateImplementation implements BlockApiDelegate {
    @Override
    public ResponseEntity<BlockResponse> block(BlockRequest blockRequest) {
        return BlockApiDelegate.super.block(blockRequest);
    }

    @Override
    public ResponseEntity<BlockTransactionResponse> blockTransaction(BlockTransactionRequest blockTransactionRequest) {
        return BlockApiDelegate.super.blockTransaction(blockTransactionRequest);
    }
}
