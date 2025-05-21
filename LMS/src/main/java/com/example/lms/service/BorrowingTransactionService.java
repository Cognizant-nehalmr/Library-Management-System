package com.example.lms.service;

import com.example.lms.entity.BorrowingTransaction;
import com.example.lms.repository.BorrowingTransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class BorrowingTransactionService {

    @Autowired
    private BorrowingTransactionRepository transactionRepository;

    public List<BorrowingTransaction> getAllTransactions() {
        return transactionRepository.findAll();
    }

    public Optional<BorrowingTransaction> getTransactionById(Long id) {
        return transactionRepository.findById(id);
    }

    public BorrowingTransaction borrowBook(BorrowingTransaction transaction) {
        transaction.setBorrowDate(LocalDate.now());
        transaction.setStatus(BorrowingTransaction.Status.BORROWED);
        return transactionRepository.save(transaction);
    }

    public BorrowingTransaction returnBook(Long transactionId) {
        Optional<BorrowingTransaction> transactionOpt = transactionRepository.findById(transactionId);
        if (transactionOpt.isPresent()) {
            BorrowingTransaction transaction = transactionOpt.get();
            transaction.setReturnDate(LocalDate.now());
            transaction.setStatus(BorrowingTransaction.Status.RETURNED);
            return transactionRepository.save(transaction);
        }
        throw new IllegalArgumentException("Transaction not found");
    }

    public Optional<BorrowingTransaction> updateTransaction(Long id, BorrowingTransaction transaction) {
        return transactionRepository.findById(id).map(existingTransaction -> {
            existingTransaction.setBorrowDate(transaction.getBorrowDate());
            existingTransaction.setReturnDate(transaction.getReturnDate());
            existingTransaction.setStatus(transaction.getStatus());
            return transactionRepository.save(existingTransaction);
        });
    }

    public List<BorrowingTransaction> getTransactionsByStatus(String status) {
        return transactionRepository.findAll().stream()
                .filter(transaction -> transaction.getStatus().name().equalsIgnoreCase(status))
                .collect(Collectors.toList());
    }
}