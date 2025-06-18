package com.library.transaction.repository;

import com.library.transaction.entity.BorrowingTransaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface BorrowingTransactionRepository extends JpaRepository<BorrowingTransaction, Long> {
    
    List<BorrowingTransaction> findByMemberId(Long memberId);
    
    List<BorrowingTransaction> findByBookId(Long bookId);
    
    List<BorrowingTransaction> findByStatus(BorrowingTransaction.TransactionStatus status);
    
    long countByMemberIdAndStatus(Long memberId, BorrowingTransaction.TransactionStatus status);
    
    @Query("SELECT t FROM BorrowingTransaction t WHERE t.dueDate < :currentDate AND t.status = 'BORROWED'")
    List<BorrowingTransaction> findOverdueTransactions(@Param("currentDate") LocalDate currentDate);
    
    @Query("SELECT t FROM BorrowingTransaction t WHERE t.memberId = :memberId AND t.status IN ('BORROWED', 'OVERDUE')")
    List<BorrowingTransaction> findActiveBorrowingsByMemberId(@Param("memberId") Long memberId);
}
