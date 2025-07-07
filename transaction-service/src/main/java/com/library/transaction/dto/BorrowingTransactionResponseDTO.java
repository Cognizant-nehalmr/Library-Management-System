package com.library.transaction.dto;

import com.library.transaction.entity.BorrowingTransaction;
import java.time.LocalDate;

public class BorrowingTransactionResponseDTO {
    private Long transactionId;
    private LocalDate borrowDate;
    private LocalDate dueDate;
    private LocalDate returnDate;
    private String status;
    private BookDTO book;
    private MemberDTO member;

    public BorrowingTransactionResponseDTO() {
        // Default constructor
    }

    // Constructor to map from entity + related data
    public BorrowingTransactionResponseDTO(
            BorrowingTransaction transaction,
            BookDTO book,
            MemberDTO member
    ) {
        this.transactionId = transaction.getTransactionId();
        this.borrowDate = transaction.getBorrowDate();
        this.dueDate = transaction.getDueDate();
        this.returnDate = transaction.getReturnDate();
        this.status = transaction.getStatus().toString();
        this.book = book;
        this.member = member;
    }

    // Getters & setters
    public Long getTransactionId() { return transactionId; }
    public void setTransactionId(Long transactionId) { this.transactionId = transactionId; }

    public LocalDate getBorrowDate() { return borrowDate; }
    public void setBorrowDate(LocalDate borrowDate) { this.borrowDate = borrowDate; }

    public LocalDate getDueDate() { return dueDate; }
    public void setDueDate(LocalDate dueDate) { this.dueDate = dueDate; }

    public LocalDate getReturnDate() { return returnDate; }
    public void setReturnDate(LocalDate returnDate) { this.returnDate = returnDate; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public BookDTO getBook() { return book; }
    public void setBook(BookDTO book) { this.book = book; }

    public MemberDTO getMember() { return member; }
    public void setMember(MemberDTO member) { this.member = member; }
}
