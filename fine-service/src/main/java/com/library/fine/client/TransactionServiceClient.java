package com.library.fine.client;

import com.library.fine.dto.BorrowingTransactionResponseDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.util.List;
import java.util.Map;

@FeignClient(name = "transaction-service")
public interface TransactionServiceClient {

    @GetMapping("/api/transactions/{id}")
    BorrowingTransactionResponseDTO getTransactionById(@PathVariable("id") Long transactionId);

    @GetMapping("/api/transactions")
    List<BorrowingTransactionResponseDTO> getAllTransactions();

    @PostMapping("/api/transactions/update-overdue")
    ResponseEntity<Map<String, String>> updateOverdueTransactions();

}
