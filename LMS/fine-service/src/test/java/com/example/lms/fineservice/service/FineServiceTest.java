package com.example.lms.fineservice.service;

import com.example.lms.fineservice.entity.Fine;
import com.example.lms.fineservice.repository.FineRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class FineServiceTest {

    @InjectMocks
    private FineService fineService;

    @Mock
    private FineRepository fineRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllFines() {
        // Arrange
        Fine fine1 = new Fine();
        fine1.setId(1L);
        fine1.setMemberId(101L);
        fine1.setAmount(50.0);

        Fine fine2 = new Fine();
        fine2.setId(2L);
        fine2.setMemberId(102L);
        fine2.setAmount(75.0);

        when(fineRepository.findAll()).thenReturn(Arrays.asList(fine1, fine2));

        // Act
        List<Fine> fines = fineService.getAllFines();

        // Assert
        assertEquals(2, fines.size());
        verify(fineRepository, times(1)).findAll();
    }

    @Test
    void testAddFine() {
        // Arrange
        Fine fine = new Fine();
        fine.setId(1L);
        fine.setMemberId(101L);
        fine.setAmount(50.0);

        when(fineRepository.save(fine)).thenReturn(fine);

        // Act
        Fine savedFine = fineService.addFine(fine);

        // Assert
        assertEquals(50.0, savedFine.getAmount());
        verify(fineRepository, times(1)).save(fine);
    }
}