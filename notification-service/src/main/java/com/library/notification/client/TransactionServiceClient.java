package com.library.notification.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;

import com.library.notification.dto.BorrowingTransactionResponseDTO;

import java.util.List;
import java.util.Map;

@FeignClient(name = "transaction-service")
public interface TransactionServiceClient {
    
    @GetMapping("/api/transactions/overdue")
    ResponseEntity<List<Map<String, Object>>> getOverdueTransactions();

    @GetMapping("/api/transactions/{id}")
    ResponseEntity<Map<String, Object>> getTransactionById(Long id);

    @GetMapping("/api/transactions")
    List<BorrowingTransactionResponseDTO> getAllTransactions();
    
}
