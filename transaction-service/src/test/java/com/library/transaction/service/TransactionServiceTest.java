package com.library.transaction.service;

import com.library.transaction.dto.BorrowingTransactionDTO;
import com.library.transaction.dto.BorrowingTransactionResponseDTO;
import com.library.transaction.entity.BorrowingTransaction;
import com.library.transaction.repository.BorrowingTransactionRepository;
import com.library.transaction.client.BookServiceClient;
import com.library.transaction.client.MemberServiceClient;
import com.library.transaction.dto.BookDTO;
import com.library.transaction.dto.MemberDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import java.time.LocalDate;
import java.util.Optional;
import java.util.Collections;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TransactionServiceTest {
    @Mock
    private BorrowingTransactionRepository transactionRepository;
    @Mock
    private BookServiceClient bookServiceClient;
    @Mock
    private MemberServiceClient memberServiceClient;
    @InjectMocks
    private TransactionService transactionService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetTransactionById_Success() {
        BorrowingTransaction tx = new BorrowingTransaction();
        tx.setTransactionId(1L);
        tx.setBookId(2L);
        tx.setMemberId(3L);
        when(transactionRepository.findById(1L)).thenReturn(Optional.of(tx));

        BookDTO book = new BookDTO();
        book.setBookId(2L);
        when(bookServiceClient.getBookById(2L)).thenReturn(org.springframework.http.ResponseEntity.ok(book));

        MemberDTO member = new MemberDTO();
        member.setMemberId(3L);
        when(memberServiceClient.getMemberById(3L)).thenReturn(org.springframework.http.ResponseEntity.ok(member));

        Optional<BorrowingTransactionResponseDTO> result = transactionService.getTransactionById(1L);
        assertTrue(result.isPresent());
        assertEquals(1L, result.get().getTransactionId());
        assertEquals(2L, result.get().getBook().getBookId());
        assertEquals(3L, result.get().getMember().getMemberId());
    }

    @Test
    void testGetTransactionById_NotFound() {
        when(transactionRepository.findById(2L)).thenReturn(Optional.empty());
        Optional<BorrowingTransactionResponseDTO> result = transactionService.getTransactionById(2L);
        assertFalse(result.isPresent());
    }

    @Test
    void testGetAllTransactions_Empty() {
        when(transactionRepository.findAll()).thenReturn(Collections.emptyList());
        List<BorrowingTransactionResponseDTO> result = transactionService.getAllTransactions();
        assertTrue(result.isEmpty());
    }

    // Add more tests for borrow, return, overdue, and edge cases
}
