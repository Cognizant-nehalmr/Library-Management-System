package com.library.fine.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "fines")
public class Fine {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "fine_id")
    private Long fineId;

    @NotNull(message = "Member ID is required")
    @Column(name = "member_id", nullable = false)
    private Long memberId;

    @NotNull(message = "Transaction ID is required")
    @Column(name = "transaction_id", nullable = false)
    private Long transactionId;

    @NotNull(message = "Amount is required")
    @DecimalMin(value = "0.0", inclusive = false, message = "Amount must be greater than 0")
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal amount;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", length = 20, nullable = false)
    private FineStatus status = FineStatus.PENDING;

    @Column(name = "transaction_date")
    private LocalDateTime transactionDate;

    @Column(name = "paid_date")
    private LocalDateTime paidDate;

    @Column(name = "fine_type")
    @Enumerated(EnumType.STRING)
    @NotNull(message = "Fine type is required")
    private FineType fineType;

    @PrePersist
    protected void onCreate() {
        transactionDate = LocalDateTime.now();
    }

    // Constructors
    public Fine() {
    }

    public Fine(Long memberId, Long transactionId, BigDecimal amount, FineType fineType) {
        this.memberId = memberId;
        this.transactionId = transactionId;
        this.amount = amount;
        this.fineType = fineType;
    }

    // Getters and Setters
    public Long getFineId() {
        return fineId;
    }

    public void setFineId(Long fineId) {
        this.fineId = fineId;
    }

    public Long getMemberId() {
        return memberId;
    }

    public void setMemberId(Long memberId) {
        this.memberId = memberId;
    }

    public Long getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(Long transactionId) {
        this.transactionId = transactionId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public FineStatus getStatus() {
        return status;
    }

    public void setStatus(FineStatus status) {
        this.status = status;
    }

    public LocalDateTime getTransactionDate() {
        return transactionDate;
    }

    public void setTransactionDate(LocalDateTime transactionDate) {
        this.transactionDate = transactionDate;
    }

    public LocalDateTime getPaidDate() {
        return paidDate;
    }

    public void setPaidDate(LocalDateTime paidDate) {
        this.paidDate = paidDate;
    }

    public FineType getFineType() {
        return fineType;
    }

    public void setFineType(FineType fineType) {
        this.fineType = fineType;
    }

    public enum FineStatus {
        PENDING, PAID, CANCELLED
    }

    public enum FineType {
        LATE_RETURN, LOST_ITEM, DAMAGED_ITEM
    }

}
