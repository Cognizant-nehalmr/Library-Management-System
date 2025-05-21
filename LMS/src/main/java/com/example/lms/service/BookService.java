package com.example.lms.service;

import com.example.lms.entity.Book;
import com.example.lms.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class BookService {

    private static final Logger logger = LoggerFactory.getLogger(BookService.class);

    @Autowired
    private BookRepository bookRepository;

    public List<Book> getAllBooks() {
        logger.info("Fetching all books");
        return bookRepository.findAll();
    }

    public Optional<Book> getBookById(Long id) {
        logger.info("Fetching book with ID: {}", id);
        return bookRepository.findById(id);
    }

    public Book saveBook(Book book) {
        logger.info("Saving book: {}", book);
        return bookRepository.save(book);
    }

    public void deleteBook(Long id) {
        logger.info("Deleting book with ID: {}", id);
        bookRepository.deleteById(id);
    }

    public Optional<Book> updateBook(Long id, Book book) {
        logger.info("Updating book with ID: {}", id);
        return bookRepository.findById(id).map(existingBook -> {
            existingBook.setTitle(book.getTitle());
            existingBook.setAuthor(book.getAuthor());
            existingBook.setGenre(book.getGenre());
            existingBook.setIsbn(book.getIsbn());
            existingBook.setYearPublished(book.getYearPublished());
            existingBook.setAvailableCopies(book.getAvailableCopies());
            return bookRepository.save(existingBook);
        });
    }

    public List<Book> searchBooks(String title, String author) {
        logger.info("Searching books with title: {} and author: {}", title, author);
        if (title != null && author != null) {
            return bookRepository.findByTitleContainingAndAuthorContaining(title, author);
        } else if (title != null) {
            return bookRepository.findByTitleContaining(title);
        } else if (author != null) {
            return bookRepository.findByAuthorContaining(author);
        } else {
            return bookRepository.findAll();
        }
    }

    // Example usage of Book fields in service logic
    public List<String> getBookDetails() {
        logger.info("Fetching book details");
        return bookRepository.findAll().stream()
                .map(book -> "Title: " + book.getTitle() + ", Author: " + book.getAuthor() + ", ISBN: " + book.getIsbn())
                .collect(Collectors.toList());
    }

    // Directly access fields in a method to ensure they are utilized
    public void printBookDetails() {
        bookRepository.findAll().forEach(book -> {
            System.out.println("Book ID: " + book.getBookId());
            System.out.println("Title: " + book.getTitle());
            System.out.println("Author: " + book.getAuthor());
            System.out.println("Genre: " + book.getGenre());
            System.out.println("ISBN: " + book.getIsbn());
            System.out.println("Year Published: " + book.getYearPublished());
            System.out.println("Available Copies: " + book.getAvailableCopies());
        });
    }
}