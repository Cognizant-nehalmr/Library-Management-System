package com.example.lms.borrowingtransactionservice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.example.lms.borrowingtransactionservice.service.BorrowingTransactionService;
import com.example.lms.borrowingtransactionservice.entity.BorrowingTransaction;
import java.util.List;

@RestController
@RequestMapping("/transactions")
public class BorrowingTransactionController {

    @Autowired
    private BorrowingTransactionService transactionService;

    @GetMapping
    public List<BorrowingTransaction> getAllTransactions() {
        return transactionService.getAllTransactions();
    }

    @PostMapping
    public BorrowingTransaction addTransaction(@RequestBody BorrowingTransaction transaction) {
        return transactionService.addTransaction(transaction);
    }

    // Additional endpoints can be added here
}