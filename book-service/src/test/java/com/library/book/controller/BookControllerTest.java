package com.library.book.controller;

import com.library.book.dto.BookDTO;
import com.library.book.service.BookService;
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

@WebMvcTest(BookController.class)
class BookControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private BookService bookService;

    @Test
    void getBookById_ReturnsBook() throws Exception {
        BookDTO book = new BookDTO();
        book.setBookId(1L);
        book.setTitle("Test Book");
        Mockito.when(bookService.getBookById(1L)).thenReturn(Optional.of(book));
        mockMvc.perform(get("/api/books/1"))
                .andExpect(status().isOk());
    }

    @Test
    void createBook_ReturnsCreated() throws Exception {
        BookDTO book = new BookDTO();
        book.setTitle("New Book");
        book.setAuthor("Author");
        book.setAvailableCopies(1);
        book.setTotalCopies(1);
        Mockito.when(bookService.createBook(Mockito.any())).thenReturn(book);
        mockMvc.perform(post("/api/books")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"title\":\"New Book\",\"author\":\"Author\",\"availableCopies\":1,\"totalCopies\":1}"))
                .andExpect(status().isCreated());
    }

    @Test
    void updateBook_ReturnsOk() throws Exception {
        BookDTO book = new BookDTO();
        book.setBookId(1L);
        book.setTitle("Updated");
        book.setAuthor("Author");
        book.setAvailableCopies(2);
        book.setTotalCopies(2);
        Mockito.when(bookService.updateBook(anyLong(), Mockito.any())).thenReturn(Optional.of(book));
        mockMvc.perform(put("/api/books/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"title\":\"Updated\",\"author\":\"Author\",\"availableCopies\":2,\"totalCopies\":2}"))
                .andExpect(status().isOk());
    }

    @Test
    void deleteBook_ReturnsNoContent() throws Exception {
        Mockito.when(bookService.deleteBook(1L)).thenReturn(true);
        mockMvc.perform(delete("/api/books/1"))
                .andExpect(status().isNoContent());
    }
}
