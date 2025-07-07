package com.library.transaction.controller;

import com.library.transaction.dto.BorrowingTransactionDTO;
import com.library.transaction.dto.BorrowingTransactionResponseDTO;
import com.library.transaction.repository.BorrowingTransactionRepository;
import com.library.transaction.service.TransactionService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/transactions")
@CrossOrigin(origins = "*")
public class TransactionController {

    @Autowired
    private TransactionService transactionService;

    @GetMapping
    public ResponseEntity<List<BorrowingTransactionResponseDTO>> getAllTransactions() {
        List<BorrowingTransactionResponseDTO> transactions = transactionService.getAllTransactions();
        return ResponseEntity.ok(transactions);
    }

    @GetMapping("/{id}")
    public ResponseEntity<BorrowingTransactionResponseDTO> getTransactionById(@PathVariable Long id) {
        return transactionService.getTransactionById(id)
                .map(transaction -> ResponseEntity.ok(transaction))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/member/{memberId}")
    public ResponseEntity<List<BorrowingTransactionResponseDTO>> getTransactionsByMemberId(@PathVariable Long memberId) {
        List<BorrowingTransactionResponseDTO> transactions = transactionService.getTransactionsByMemberId(memberId);
        return ResponseEntity.ok(transactions);
    }

    @GetMapping("/book/{bookId}")
    public ResponseEntity<List<BorrowingTransactionResponseDTO>> getTransactionsByBookId(@PathVariable Long bookId) {
        List<BorrowingTransactionResponseDTO> transactions = transactionService.getTransactionsByBookId(bookId);
        return ResponseEntity.ok(transactions);
    }

    // This Method Is used in Service Explicitly For Updating Overdue Transactions
    // It's
//    @GetMapping("/overdue")
//    public ResponseEntity<List<BorrowingTransactionResponseDTO>> getOverdueTransactions() {
//        List<BorrowingTransactionResponseDTO> transactions = transactionService.getOverdueTransactions();
//        return ResponseEntity.ok(transactions);
//    }

    @PostMapping("/borrow")
    public ResponseEntity<?> borrowBook(@Valid @RequestBody BorrowingTransactionDTO transactionDTO) {
        try {
            BorrowingTransactionResponseDTO borrowedTransaction = transactionService.borrowBook(transactionDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(borrowedTransaction);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @PutMapping("/{id}/return")
    public ResponseEntity<?> returnBook(@PathVariable Long id) {
        try {
            return transactionService.returnBook(id)
                    .map(ResponseEntity::ok)
                    .orElse(ResponseEntity.notFound().build());
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @PostMapping("/update-overdue")
    public ResponseEntity<Map<String, String>> updateOverdueTransactions() {
        transactionService.updateOverdueTransactions();
        return ResponseEntity.ok(Map.of("message", "Overdue transactions updated successfully"));
    }
}
