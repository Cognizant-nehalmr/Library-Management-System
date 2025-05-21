package com.example.lms.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;

@Data
@Entity
@Table(name = "fines")
public class Fine {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long fineId;

    @ManyToOne
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    private double amount;

    @Enumerated(EnumType.STRING)
    private Status status;

    private LocalDate transactionDate;

    public enum Status {
        PAID,
        PENDING
    }
}