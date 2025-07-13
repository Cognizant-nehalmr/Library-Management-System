package com.library.fine.controller;

import com.library.fine.dto.FineResponseDTO;
import com.library.fine.service.FineService;
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

@WebMvcTest(FineController.class)
class FineControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private FineService fineService;

    @Test
    void getFineById_ReturnsFine() throws Exception {
        FineResponseDTO fine = new FineResponseDTO();
        Mockito.when(fineService.getFineById(1L)).thenReturn(Optional.of(fine));
        mockMvc.perform(get("/api/fines/1"))
                .andExpect(status().isOk());
    }

    @Test
    void createFine_ReturnsCreated() throws Exception {
        FineResponseDTO fine = new FineResponseDTO();
        Mockito.when(fineService.createFine(Mockito.anyLong(), Mockito.any(), Mockito.any())).thenReturn(fine);
        mockMvc.perform(post("/api/fines/1/LATE_RETURN"))
                .andExpect(status().isOk());
    }

    @Test
    void payFine_ReturnsOk() throws Exception {
        FineResponseDTO fine = new FineResponseDTO();
        Mockito.when(fineService.payFine(anyLong())).thenReturn(Optional.of(fine));
        mockMvc.perform(put("/api/fines/1/pay"))
                .andExpect(status().isOk());
    }
}
