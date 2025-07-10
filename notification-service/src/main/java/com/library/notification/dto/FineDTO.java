package com.library.notification.dto;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public class FineDTO {
    private Long fineId;

    @NotNull(message = "Member ID is required")
    private Long memberId;

    @NotNull(message = "Transaction ID is required")
    private Long transactionId;

    @NotNull(message = "Amount is required")
    @DecimalMin(value = "0.0", inclusive = false, message = "Amount must be greater than 0")
    private BigDecimal amount;

    private FineStatus status;
    private LocalDateTime transactionDate;
    private LocalDateTime paidDate;
    private FineType fineType;

    // Constructors
    public FineDTO() {
    }

    public FineDTO(Long memberId, Long transactionId, BigDecimal amount, FineType fineType) {
        this.memberId = memberId;
        this.transactionId = transactionId;
        this.amount = amount;
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
