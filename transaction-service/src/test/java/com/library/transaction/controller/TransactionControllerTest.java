package com.library.transaction.controller;

import com.library.transaction.dto.BorrowingTransactionResponseDTO;
import com.library.transaction.service.TransactionService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import java.util.Optional;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(TransactionController.class)
class TransactionControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private TransactionService transactionService;

    @Test
    void getTransactionById_ReturnsTransaction() throws Exception {
        BorrowingTransactionResponseDTO tx = new BorrowingTransactionResponseDTO();
        tx.setTransactionId(1L);
        Mockito.when(transactionService.getTransactionById(1L)).thenReturn(Optional.of(tx));
        mockMvc.perform(get("/api/transactions/1"))
                .andExpect(status().isOk());
    }

    @Test
    void borrowBook_ReturnsCreated() throws Exception {
        BorrowingTransactionResponseDTO tx = new BorrowingTransactionResponseDTO();
        Mockito.when(transactionService.borrowBook(Mockito.any())).thenReturn(tx);
        String today = java.time.LocalDate.now().toString();
        String due = java.time.LocalDate.now().plusDays(14).toString();
        String payload = String.format("{\"memberId\":1,\"bookId\":1,\"borrowDate\":\"%s\",\"dueDate\":\"%s\"}", today, due);
        mockMvc.perform(post("/api/transactions/borrow")
                .contentType(MediaType.APPLICATION_JSON)
                .content(payload))
                .andExpect(status().isCreated());
    }

    @Test
    void returnBook_ReturnsOk() throws Exception {
        BorrowingTransactionResponseDTO tx = new BorrowingTransactionResponseDTO();
        Mockito.when(transactionService.returnBook(anyLong())).thenReturn(Optional.of(tx));
        mockMvc.perform(put("/api/transactions/1/return"))
                .andExpect(status().isOk());
    }
}
