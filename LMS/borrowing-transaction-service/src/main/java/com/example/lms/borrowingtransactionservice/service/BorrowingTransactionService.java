package com.example.lms.borrowingtransactionservice.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.lms.borrowingtransactionservice.repository.BorrowingTransactionRepository;
import com.example.lms.borrowingtransactionservice.entity.BorrowingTransaction;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class BorrowingTransactionService {

    private static final Logger logger = LoggerFactory.getLogger(BorrowingTransactionService.class);

    @Autowired
    private BorrowingTransactionRepository transactionRepository;

    public List<BorrowingTransaction> getAllTransactions() {
        logger.info("Fetching all borrowing transactions");
        return transactionRepository.findAll();
    }

    public BorrowingTransaction addTransaction(BorrowingTransaction transaction) {
        logger.info("Adding a new transaction for member ID: {}", transaction.getMemberId());
        return transactionRepository.save(transaction);
    }

    // Additional service methods can be added here
}