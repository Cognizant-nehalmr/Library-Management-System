package com.library.transaction.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.library.transaction.dto.BookDTO;

import java.util.Map;

@FeignClient(name = "book-service")
public interface BookServiceClient {

    @GetMapping("/api/books/{id}")
    ResponseEntity<BookDTO> getBookById(@PathVariable("id") Long bookId);

    @PutMapping("/api/books/{id}/availability")
    ResponseEntity<Map<String, String>> updateBookAvailability(
            @PathVariable("id") Long bookId,
            @RequestBody Map<String, Integer> request);
}
