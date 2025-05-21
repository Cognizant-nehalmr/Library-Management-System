package com.example.lms.controller;

import com.example.lms.entity.Fine;
import com.example.lms.service.FineService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(classes = com.example.lms.LmsApplication.class)
@AutoConfigureMockMvc
class FineControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private FineService fineService;

    @Test
    void testGetAllFines() throws Exception {
        when(fineService.getAllFines()).thenReturn(List.of(new Fine()));

        mockMvc.perform(get("/api/fines"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    void testPayFine() throws Exception {
        Fine fine = new Fine();
        when(fineService.payFine(anyLong())).thenReturn(fine);

        mockMvc.perform(post("/api/fines/pay/1"))
                .andExpect(status().isOk());
    }
}