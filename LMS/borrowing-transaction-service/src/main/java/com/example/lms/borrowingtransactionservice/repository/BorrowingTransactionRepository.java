package com.example.lms.borrowingtransactionservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.lms.borrowingtransactionservice.entity.BorrowingTransaction;

public interface BorrowingTransactionRepository extends JpaRepository<BorrowingTransaction, Long> {
    // Additional query methods can be added here
}