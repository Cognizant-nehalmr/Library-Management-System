package com.example.lms.borrowingtransactionservice.service;

import com.example.lms.borrowingtransactionservice.entity.BorrowingTransaction;
import com.example.lms.borrowingtransactionservice.repository.BorrowingTransactionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class BorrowingTransactionServiceTest {

    @InjectMocks
    private BorrowingTransactionService transactionService;

    @Mock
    private BorrowingTransactionRepository transactionRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllTransactions() {
        // Arrange
        BorrowingTransaction transaction1 = new BorrowingTransaction();
        transaction1.setId(1L);
        transaction1.setMemberId(101L);
        transaction1.setBookId(201L);

        BorrowingTransaction transaction2 = new BorrowingTransaction();
        transaction2.setId(2L);
        transaction2.setMemberId(102L);
        transaction2.setBookId(202L);

        when(transactionRepository.findAll()).thenReturn(Arrays.asList(transaction1, transaction2));

        // Act
        List<BorrowingTransaction> transactions = transactionService.getAllTransactions();

        // Assert
        assertEquals(2, transactions.size());
        verify(transactionRepository, times(1)).findAll();
    }

    @Test
    void testAddTransaction() {
        // Arrange
        BorrowingTransaction transaction = new BorrowingTransaction();
        transaction.setId(1L);
        transaction.setMemberId(101L);
        transaction.setBookId(201L);

        when(transactionRepository.save(transaction)).thenReturn(transaction);

        // Act
        BorrowingTransaction savedTransaction = transactionService.addTransaction(transaction);

        // Assert
        assertEquals(101L, savedTransaction.getMemberId());
        verify(transactionRepository, times(1)).save(transaction);
    }
}