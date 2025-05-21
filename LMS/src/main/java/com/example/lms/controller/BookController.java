package com.example.lms.controller;

import com.example.lms.entity.Book;
import com.example.lms.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/books")
public class BookController {

    private static final Logger logger = LoggerFactory.getLogger(BookController.class);

    @Autowired
    private BookService bookService;

    @GetMapping
    public List<Book> getAllBooks() {
        logger.info("Received request to fetch all books");
        return bookService.getAllBooks();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Book> getBookById(@PathVariable Long id) {
        logger.info("Received request to fetch book with ID: {}", id);
        return bookService.getBookById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> {
                    logger.warn("Book with ID: {} not found", id);
                    return ResponseEntity.notFound().build();
                });
    }

    @PostMapping
    public Book createBook(@RequestBody Book book) {
        logger.info("Received request to create book: {}", book);
        return bookService.saveBook(book);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBook(@PathVariable Long id) {
        logger.info("Received request to delete book with ID: {}", id);
        bookService.deleteBook(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Book> updateBook(@PathVariable Long id, @RequestBody Book book) {
        logger.info("Received request to update book with ID: {}", id);
        Optional<Book> updatedBook = bookService.updateBook(id, book);
        return updatedBook.map(ResponseEntity::ok)
                .orElseGet(() -> {
                    logger.warn("Book with ID: {} not found", id);
                    return ResponseEntity.notFound().build();
                });
    }

    @GetMapping("/search")
    public List<Book> searchBooks(@RequestParam(required = false) String title, @RequestParam(required = false) String author) {
        logger.info("Received request to search books with title: {} and author: {}", title, author);
        return bookService.searchBooks(title, author);
    }
}