package com.library.fine.service;

import com.library.fine.client.TransactionServiceClient;
import com.library.fine.dto.BookDTO;
import com.library.fine.dto.BorrowingTransactionResponseDTO;
import com.library.fine.dto.FineResponseDTO;
import com.library.fine.dto.MemberDTO;
import com.library.fine.entity.Fine;
import com.library.fine.entity.Fine.FineType;
import com.library.fine.repository.FineRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;
import java.util.Collections;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class FineServiceTest {
    @Mock
    private FineRepository fineRepository;
    @Mock
    private TransactionServiceClient transactionServiceClient;
    @InjectMocks
    private FineService fineService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetFineById_Success() {
        Fine fine = new Fine();
        fine.setFineId(1L);
        fine.setAmount(new BigDecimal("5.00"));
        when(fineRepository.findById(1L)).thenReturn(Optional.of(fine));
        Optional<FineResponseDTO> result = fineService.getFineById(1L);
        assertTrue(result.isPresent());
        assertEquals(1L, result.get().getFineDTO().getFineId());
        assertEquals(new BigDecimal("5.00"), result.get().getFineDTO().getAmount());
    }

    @Test
    void testGetFineById_NotFound() {
        when(fineRepository.findById(2L)).thenReturn(Optional.empty());
        Optional<FineResponseDTO> result = fineService.getFineById(2L);
        assertFalse(result.isPresent());
    }

    @Test
    void testGetAllFines_Empty() {
        when(fineRepository.findAll()).thenReturn(Collections.emptyList());
        List<FineResponseDTO> result = fineService.getAllFines();
        assertTrue(result.isEmpty());
    }

    @Test
    void testCreateFine_Success() {
        when(fineRepository.existsByTransactionId(100L)).thenReturn(false);

        Fine fine = new Fine();
        fine.setFineId(10L);
        fine.setAmount(new BigDecimal("2.00"));

        // ✅ Mock transaction response
        BorrowingTransactionResponseDTO mockTransaction = new BorrowingTransactionResponseDTO();
        mockTransaction.setTransactionId(100L);
        mockTransaction.setDueDate(LocalDate.now().minusDays(1)); // set some due date
        mockTransaction.setReturnDate(LocalDate.now());
        MemberDTO mockMember = new MemberDTO();
        mockMember.setMemberId(1L);
        mockMember.setName("John Doe");
        mockTransaction.setMember(mockMember);
        BookDTO mockBook = new BookDTO();
        mockBook.setBookId(1L);
        mockBook.setTitle("Test Book");
        mockTransaction.setBook(mockBook);

        when(transactionServiceClient.getTransactionById(100L))
                .thenReturn(mockTransaction);

        when(fineRepository.save(any(Fine.class))).thenReturn(fine);

        FineResponseDTO result = fineService.createFine(100L, FineType.DAMAGED_ITEM, null);

        assertNotNull(result);
        assertEquals(new BigDecimal("2.00"), result.getFineDTO().getAmount());
    }

    @Test
    void testCreateFine_Duplicate() {
        when(fineRepository.existsByTransactionId(100L)).thenReturn(true);
        assertThrows(RuntimeException.class, () -> fineService.createFine(100L, FineType.DAMAGED_ITEM , null));
    }
}
