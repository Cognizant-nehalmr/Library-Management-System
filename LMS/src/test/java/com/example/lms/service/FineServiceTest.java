package com.example.lms.service;

import com.example.lms.entity.Fine;
import com.example.lms.repository.FineRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class FineServiceTest {

    @Mock
    private FineRepository fineRepository;

    @InjectMocks
    private FineService fineService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllFines() {
        List<Fine> fines = List.of(new Fine());
        when(fineRepository.findAll()).thenReturn(fines);

        List<Fine> result = fineService.getAllFines();

        assertEquals(1, result.size());
        verify(fineRepository, times(1)).findAll();
    }

    @Test
    void testPayFine() {
        Fine fine = new Fine();
        when(fineRepository.save(fine)).thenReturn(fine);

        Fine result = fineService.payFine(1L);

        assertNotNull(result);
        verify(fineRepository, times(1)).save(fine);
    }
}