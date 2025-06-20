package com.library.book.service;

import com.library.book.dto.BookDTO;
import com.library.book.entity.Book;
import com.library.book.repository.BookRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import java.util.Optional;
import java.util.Collections;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class BookServiceTest {
    @Mock
    private BookRepository bookRepository;
    @InjectMocks
    private BookService bookService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetBookById_Success() {
        Book book = new Book();
        book.setBookId(1L);
        book.setTitle("Test Book");
        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));
        Optional<BookDTO> result = bookService.getBookById(1L);
        assertTrue(result.isPresent());
        assertEquals(1L, result.get().getBookId());
        assertEquals("Test Book", result.get().getTitle());
    }

    @Test
    void testGetBookById_NotFound() {
        when(bookRepository.findById(2L)).thenReturn(Optional.empty());
        Optional<BookDTO> result = bookService.getBookById(2L);
        assertFalse(result.isPresent());
    }

    @Test
    void testGetAllBooks_Empty() {
        when(bookRepository.findAll()).thenReturn(Collections.emptyList());
        List<BookDTO> result = bookService.getAllBooks();
        assertTrue(result.isEmpty());
    }

    @Test
    void testCreateBook() {
        BookDTO dto = new BookDTO();
        dto.setTitle("New Book");
        dto.setAuthor("Author");
        dto.setAvailableCopies(2);
        dto.setTotalCopies(2);
        Book book = new Book();
        book.setBookId(10L);
        book.setTitle("New Book");
        when(bookRepository.save(any(Book.class))).thenReturn(book);
        BookDTO saved = bookService.createBook(dto);
        assertNotNull(saved);
        assertEquals("New Book", saved.getTitle());
    }

    @Test
    void testUpdateBook_Success() {
        Book existing = new Book();
        existing.setBookId(1L);
        existing.setTitle("Old");
        BookDTO update = new BookDTO();
        update.setTitle("Updated");
        update.setAvailableCopies(1);
        update.setTotalCopies(1);
        when(bookRepository.findById(1L)).thenReturn(Optional.of(existing));
        when(bookRepository.save(any(Book.class))).thenReturn(existing);
        Optional<BookDTO> result = bookService.updateBook(1L, update);
        assertTrue(result.isPresent());
        assertEquals("Updated", result.get().getTitle());
    }

    @Test
    void testUpdateBook_NotFound() {
        BookDTO update = new BookDTO();
        when(bookRepository.findById(99L)).thenReturn(Optional.empty());
        Optional<BookDTO> result = bookService.updateBook(99L, update);
        assertFalse(result.isPresent());
    }

    @Test
    void testDeleteBook_Success() {
        when(bookRepository.existsById(1L)).thenReturn(true);
        doNothing().when(bookRepository).deleteById(1L);
        assertTrue(bookService.deleteBook(1L));
    }

    @Test
    void testDeleteBook_NotFound() {
        when(bookRepository.existsById(2L)).thenReturn(false);
        assertFalse(bookService.deleteBook(2L));
    }

    @Test
    void testUpdateBookAvailability_Success() {
        Book book = new Book();
        book.setBookId(1L);
        book.setAvailableCopies(2);
        book.setTotalCopies(3);
        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));
        when(bookRepository.save(any(Book.class))).thenReturn(book);
        assertTrue(bookService.updateBookAvailability(1L, -1));
        assertEquals(1, book.getAvailableCopies());
    }

    @Test
    void testUpdateBookAvailability_Invalid() {
        Book book = new Book();
        book.setBookId(1L);
        book.setAvailableCopies(0);
        book.setTotalCopies(1);
        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));
        assertFalse(bookService.updateBookAvailability(1L, -1));
    }
}
