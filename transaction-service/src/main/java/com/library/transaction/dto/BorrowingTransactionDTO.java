package com.library.transaction.dto;

import com.library.transaction.entity.BorrowingTransaction;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

public class BorrowingTransactionDTO {
    private Long transactionId;
    
    @NotNull(message = "Book ID is required")
    private Long bookId;
    
    @NotNull(message = "Member ID is required")
    private Long memberId;
    
    @NotNull(message = "Borrow date is required")
    private LocalDate borrowDate;
    
    @NotNull(message = "Due date is required")
    private LocalDate dueDate;
    
    private LocalDate returnDate;
    private BorrowingTransaction.TransactionStatus status;

    // Constructors
    public BorrowingTransactionDTO() {}

    public BorrowingTransactionDTO(Long bookId, Long memberId, LocalDate borrowDate, LocalDate dueDate) {
        this.bookId = bookId;
        this.memberId = memberId;
        this.borrowDate = borrowDate;
        this.dueDate = dueDate;
    }

    // Getters and Setters
    public Long getTransactionId() { return transactionId; }
    public void setTransactionId(Long transactionId) { this.transactionId = transactionId; }

    public Long getBookId() { return bookId; }
    public void setBookId(Long bookId) { this.bookId = bookId; }

    public Long getMemberId() { return memberId; }
    public void setMemberId(Long memberId) { this.memberId = memberId; }

    public LocalDate getBorrowDate() { return borrowDate; }
    public void setBorrowDate(LocalDate borrowDate) { this.borrowDate = borrowDate; }

    public LocalDate getDueDate() { return dueDate; }
    public void setDueDate(LocalDate dueDate) { this.dueDate = dueDate; }

    public LocalDate getReturnDate() { return returnDate; }
    public void setReturnDate(LocalDate returnDate) { this.returnDate = returnDate; }

    public BorrowingTransaction.TransactionStatus getStatus() { return status; }
    public void setStatus(BorrowingTransaction.TransactionStatus status) { this.status = status; }
}
