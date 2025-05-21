package com.example.lms.bookservice.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.lms.bookservice.repository.BookRepository;
import com.example.lms.bookservice.entity.Book;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class BookService {

    private static final Logger logger = LoggerFactory.getLogger(BookService.class);

    @Autowired
    private BookRepository bookRepository;

    public List<Book> getAllBooks() {
        logger.info("Fetching all books");
        return bookRepository.findAll();
    }

    public Book addBook(Book book) {
        logger.info("Adding a new book: {}", book.getTitle());
        return bookRepository.save(book);
    }

    // Additional service methods can be added here
}