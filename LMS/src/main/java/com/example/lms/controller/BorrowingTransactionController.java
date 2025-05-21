package com.example.lms.controller;

import com.example.lms.entity.BorrowingTransaction;
import com.example.lms.service.BorrowingTransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/transactions")
public class BorrowingTransactionController {

    @Autowired
    private BorrowingTransactionService transactionService;

    @GetMapping
    public List<BorrowingTransaction> getAllTransactions() {
        return transactionService.getAllTransactions();
    }

    @GetMapping("/{id}")
    public ResponseEntity<BorrowingTransaction> getTransactionById(@PathVariable Long id) {
        Optional<BorrowingTransaction> transaction = transactionService.getTransactionById(id);
        return transaction.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping("/borrow")
    public BorrowingTransaction borrowBook(@RequestBody BorrowingTransaction transaction) {
        return transactionService.borrowBook(transaction);
    }

    @PostMapping("/return/{id}")
    public ResponseEntity<BorrowingTransaction> returnBook(@PathVariable Long id) {
        try {
            BorrowingTransaction transaction = transactionService.returnBook(id);
            return ResponseEntity.ok(transaction);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<BorrowingTransaction> updateTransaction(@PathVariable Long id, @RequestBody BorrowingTransaction transaction) {
        Optional<BorrowingTransaction> updatedTransaction = transactionService.updateTransaction(id, transaction);
        return updatedTransaction.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/status")
    public List<BorrowingTransaction> getTransactionsByStatus(@RequestParam String status) {
        return transactionService.getTransactionsByStatus(status);
    }
}