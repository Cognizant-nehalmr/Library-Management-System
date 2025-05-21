package com.example.lms.controller;

import com.example.lms.entity.BorrowingTransaction;
import com.example.lms.service.BorrowingTransactionService;
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
class BorrowingTransactionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BorrowingTransactionService transactionService;

    @Test
    void testGetAllTransactions() throws Exception {
        when(transactionService.getAllTransactions()).thenReturn(List.of(new BorrowingTransaction()));

        mockMvc.perform(get("/api/transactions"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    void testBorrowBook() throws Exception {
        BorrowingTransaction transaction = new BorrowingTransaction();
        when(transactionService.borrowBook(any(BorrowingTransaction.class))).thenReturn(transaction);

        String transactionJson = "{\"bookId\":1,\"memberId\":1}";

        mockMvc.perform(post("/api/transactions/borrow")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(transactionJson))
                .andExpect(status().isOk());
    }
}