package com.library.fine.client;

import com.library.fine.dto.BorrowingTransactionResponseDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;


import java.util.Map;

@FeignClient(name = "transaction-service")
public interface TransactionServiceClient {

    @GetMapping("/api/transactions/{id}")
    BorrowingTransactionResponseDTO getTransactionById(@PathVariable("id") Long transactionId);

}
