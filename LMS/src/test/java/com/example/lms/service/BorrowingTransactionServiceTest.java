package com.example.lms.service;

import com.example.lms.entity.BorrowingTransaction;
import com.example.lms.repository.BorrowingTransactionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class BorrowingTransactionServiceTest {

    @Mock
    private BorrowingTransactionRepository transactionRepository;

    @InjectMocks
    private BorrowingTransactionService transactionService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllTransactions() {
        List<BorrowingTransaction> transactions = List.of(new BorrowingTransaction());
        when(transactionRepository.findAll()).thenReturn(transactions);

        List<BorrowingTransaction> result = transactionService.getAllTransactions();

        assertEquals(1, result.size());
        verify(transactionRepository, times(1)).findAll();
    }

    @Test
    void testBorrowBook() {
        BorrowingTransaction transaction = new BorrowingTransaction();
        when(transactionRepository.save(transaction)).thenReturn(transaction);

        BorrowingTransaction result = transactionService.borrowBook(transaction);

        assertNotNull(result);
        verify(transactionRepository, times(1)).save(transaction);
    }
}