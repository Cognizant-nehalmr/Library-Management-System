package com.library.fine.repository;

import com.library.fine.entity.Fine;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface FineRepository extends JpaRepository<Fine, Long> {
    
    List<Fine> findByMemberId(Long memberId);
    
    List<Fine> findByStatus(Fine.FineStatus status);
    
    boolean existsByTransactionId(Long transactionId);
    
    @Query("SELECT COALESCE(SUM(f.amount), 0) FROM Fine f WHERE f.memberId = :memberId AND f.status = 'PENDING'")
    BigDecimal getTotalPendingFinesByMember(@Param("memberId") Long memberId);
    
    @Query("SELECT f FROM Fine f WHERE f.memberId = :memberId AND f.status = 'PENDING'")
    List<Fine> findPendingFinesByMember(@Param("memberId") Long memberId);
}
