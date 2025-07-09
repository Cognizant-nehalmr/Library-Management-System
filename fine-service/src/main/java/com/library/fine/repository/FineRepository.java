package com.library.fine.repository;

import com.library.fine.entity.Fine;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Repository
public interface FineRepository extends JpaRepository<Fine, Long> {

    List<Fine> findByMemberId(Long memberId);

    List<Fine> findByStatus(Fine.FineStatus status);

    boolean existsByTransactionId(Long transactionId);

    Optional<Fine> findByTransactionIdAndFineType(Long transactionId, Fine.FineType fineType);

    @Query("SELECT COALESCE(SUM(f.amount), 0) FROM Fine f WHERE f.memberId = :memberId AND f.status = 'PENDING'")
    BigDecimal getTotalPendingFinesByMember(@Param("memberId") Long memberId);

    @Query("SELECT f FROM Fine f WHERE f.memberId = :memberId AND f.status = 'PENDING'")
    List<Fine> findPendingFinesByMember(@Param("memberId") Long memberId);

    @Query("SELECT COALESCE(SUM(f.amount), 0) FROM Fine f WHERE f.status = 'PENDING'")
    BigDecimal getTotalPendingFines();

    @Query("SELECT COALESCE(SUM(f.amount), 0) FROM Fine f WHERE f.status = 'PAID'")
    BigDecimal getTotalCollectedFines();
}
