package com.library.book.controller;

import com.library.book.dto.BookDTO;
import com.library.book.service.BookService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/books")
@CrossOrigin(origins = "*")
@Tag(name = "Book Management", description = "APIs for managing books in the library system")
public class BookController {

    @Autowired
    private BookService bookService;

    @GetMapping
    @Operation(summary = "Get all books", description = "Retrieve a list of all books in the library")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved books"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<List<BookDTO>> getAllBooks() {
        List<BookDTO> books = bookService.getAllBooks();
        return ResponseEntity.ok(books);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get a book by ID", description = "Retrieve a specific book by its ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved the book"),
        @ApiResponse(responseCode = "404", description = "Book not found"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<BookDTO> getBookById(@Parameter(description = "Book ID") @PathVariable Long id) {
        return bookService.getBookById(id)
                .map(book -> ResponseEntity.ok(book))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/isbn/{isbn}")
    @Operation(summary = "Get a book by ISBN", description = "Retrieve a specific book by its ISBN")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved the book"),
        @ApiResponse(responseCode = "404", description = "Book not found"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<BookDTO> getBookByIsbn(@Parameter(description = "Book ISBN") @PathVariable String isbn) {
        return bookService.getBookByIsbn(isbn)
                .map(book -> ResponseEntity.ok(book))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/search")
    @Operation(summary = "Search books", description = "Search for books based on title, author, or genre")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved books"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<List<BookDTO>> searchBooks(
            @Parameter(description = "Book title") @RequestParam(required = false) String title,
            @Parameter(description = "Book author") @RequestParam(required = false) String author,
            @Parameter(description = "Book genre") @RequestParam(required = false) String genre) {
        List<BookDTO> books = bookService.searchBooks(title, author, genre);
        return ResponseEntity.ok(books);
    }

    @GetMapping("/available")
    @Operation(summary = "Get available books", description = "Retrieve a list of all available books in the library")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved books"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<List<BookDTO>> getAvailableBooks() {
        List<BookDTO> books = bookService.getAvailableBooks();
        return ResponseEntity.ok(books);
    }

    @PostMapping
    @Operation(summary = "Create a new book", description = "Create a new book in the library")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Successfully created the book"),
        @ApiResponse(responseCode = "400", description = "Bad request"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<BookDTO> createBook(@Valid @RequestBody BookDTO bookDTO) {
        BookDTO createdBook = bookService.createBook(bookDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdBook);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update a book", description = "Update an existing book in the library")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully updated the book"),
        @ApiResponse(responseCode = "400", description = "Bad request"),
        @ApiResponse(responseCode = "404", description = "Book not found"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<BookDTO> updateBook(@Parameter(description = "Book ID") @PathVariable Long id,
                                            @Valid @RequestBody BookDTO bookDTO) {
        return bookService.updateBook(id, bookDTO)
                .map(book -> ResponseEntity.ok(book))
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a book", description = "Delete a book from the library")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Successfully deleted the book"),
        @ApiResponse(responseCode = "404", description = "Book not found"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<Void> deleteBook(@Parameter(description = "Book ID") @PathVariable Long id) {
        if (bookService.deleteBook(id)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    @PutMapping("/{id}/availability")
    @Operation(summary = "Update book availability", description = "Update the availability of a specific book")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully updated book availability"),
        @ApiResponse(responseCode = "400", description = "Bad request"),
        @ApiResponse(responseCode = "404", description = "Book not found"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<Map<String, String>> updateBookAvailability(
            @Parameter(description = "Book ID") @PathVariable Long id,
            @RequestBody Map<String, Integer> request) {
        Integer change = request.get("change");
        if (change == null) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", "Change value is required"));
        }
        
        if (bookService.updateBookAvailability(id, change)) {
            return ResponseEntity.ok(Map.of("message", "Book availability updated successfully"));
        }
        return ResponseEntity.badRequest()
                .body(Map.of("error", "Unable to update book availability"));
    }
}
