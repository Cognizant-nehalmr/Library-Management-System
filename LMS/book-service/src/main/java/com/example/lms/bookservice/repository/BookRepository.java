package com.example.lms.bookservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.lms.bookservice.entity.Book;

public interface BookRepository extends JpaRepository<Book, Long> {
    // Additional query methods can be added here
}