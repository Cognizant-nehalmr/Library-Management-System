package com.example.lms.bookservice.service;

import com.example.lms.bookservice.entity.Book;
import com.example.lms.bookservice.repository.BookRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class BookServiceTest {

    @InjectMocks
    private BookService bookService;

    @Mock
    private BookRepository bookRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllBooks() {
        // Arrange
        Book book1 = new Book();
        book1.setId(1L);
        book1.setTitle("Book 1");
        book1.setAuthor("Author 1");

        Book book2 = new Book();
        book2.setId(2L);
        book2.setTitle("Book 2");
        book2.setAuthor("Author 2");

        when(bookRepository.findAll()).thenReturn(Arrays.asList(book1, book2));

        // Act
        List<Book> books = bookService.getAllBooks();

        // Assert
        assertEquals(2, books.size());
        verify(bookRepository, times(1)).findAll();
    }

    @Test
    void testAddBook() {
        // Arrange
        Book book = new Book();
        book.setId(1L);
        book.setTitle("New Book");
        book.setAuthor("New Author");

        when(bookRepository.save(book)).thenReturn(book);

        // Act
        Book savedBook = bookService.addBook(book);

        // Assert
        assertEquals("New Book", savedBook.getTitle());
        verify(bookRepository, times(1)).save(book);
    }
}