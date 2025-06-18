package com.library.transaction.service;

import com.library.transaction.client.BookServiceClient;
import com.library.transaction.dto.BorrowingTransactionDTO;
import com.library.transaction.entity.BorrowingTransaction;
import com.library.transaction.repository.BorrowingTransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class TransactionService {

    @Autowired
    private BorrowingTransactionRepository transactionRepository;

    @Autowired
    private BookServiceClient bookServiceClient;

    public List<BorrowingTransactionDTO> getAllTransactions() {
        return transactionRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public Optional<BorrowingTransactionDTO> getTransactionById(Long id) {
        return transactionRepository.findById(id)
                .map(this::convertToDTO);
    }

    public List<BorrowingTransactionDTO> getTransactionsByMemberId(Long memberId) {
        return transactionRepository.findByMemberId(memberId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<BorrowingTransactionDTO> getTransactionsByBookId(Long bookId) {
        return transactionRepository.findByBookId(bookId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<BorrowingTransactionDTO> getOverdueTransactions() {
        LocalDate today = LocalDate.now();
        return transactionRepository.findOverdueTransactions(today).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public BorrowingTransactionDTO borrowBook(BorrowingTransactionDTO transactionDTO) {
        // Check if member has reached borrowing limit
        long activeBorrowings = transactionRepository.countByMemberIdAndStatus(
                transactionDTO.getMemberId(), BorrowingTransaction.TransactionStatus.BORROWED);
        
        if (activeBorrowings >= 5) { // Assuming max 5 books per member
            throw new RuntimeException("Member has reached maximum borrowing limit");
        }

        // Update book availability
        try {
            bookServiceClient.updateBookAvailability(
                    transactionDTO.getBookId(), 
                    Map.of("change", -1));
        } catch (Exception e) {
            throw new RuntimeException("Unable to update book availability: " + e.getMessage());
        }

        BorrowingTransaction transaction = convertToEntity(transactionDTO);
        transaction.setBorrowDate(LocalDate.now());
        transaction.setDueDate(LocalDate.now().plusDays(14)); // 2 weeks borrowing period
        transaction.setStatus(BorrowingTransaction.TransactionStatus.BORROWED);

        BorrowingTransaction savedTransaction = transactionRepository.save(transaction);
        return convertToDTO(savedTransaction);
    }

    public Optional<BorrowingTransactionDTO> returnBook(Long transactionId) {
        return transactionRepository.findById(transactionId)
                .map(transaction -> {
                    if (transaction.getStatus() != BorrowingTransaction.TransactionStatus.BORROWED &&
                        transaction.getStatus() != BorrowingTransaction.TransactionStatus.OVERDUE) {
                        throw new RuntimeException("Book is not currently borrowed");
                    }

                    // Update book availability
                    try {
                        bookServiceClient.updateBookAvailability(
                                transaction.getBookId(), 
                                Map.of("change", 1));
                    } catch (Exception e) {
                        throw new RuntimeException("Unable to update book availability: " + e.getMessage());
                    }

                    transaction.setReturnDate(LocalDate.now());
                    transaction.setStatus(BorrowingTransaction.TransactionStatus.RETURNED);
                    
                    BorrowingTransaction updatedTransaction = transactionRepository.save(transaction);
                    return convertToDTO(updatedTransaction);
                });
    }

    public void updateOverdueTransactions() {
        LocalDate today = LocalDate.now();
        List<BorrowingTransaction> overdueTransactions = transactionRepository.findOverdueTransactions(today);
        
        for (BorrowingTransaction transaction : overdueTransactions) {
            if (transaction.getStatus() == BorrowingTransaction.TransactionStatus.BORROWED) {
                transaction.setStatus(BorrowingTransaction.TransactionStatus.OVERDUE);
                transactionRepository.save(transaction);
            }
        }
    }

    private BorrowingTransactionDTO convertToDTO(BorrowingTransaction transaction) {
        BorrowingTransactionDTO dto = new BorrowingTransactionDTO();
        dto.setTransactionId(transaction.getTransactionId());
        dto.setBookId(transaction.getBookId());
        dto.setMemberId(transaction.getMemberId());
        dto.setBorrowDate(transaction.getBorrowDate());
        dto.setDueDate(transaction.getDueDate());
        dto.setReturnDate(transaction.getReturnDate());
        dto.setStatus(transaction.getStatus());
        return dto;
    }

    private BorrowingTransaction convertToEntity(BorrowingTransactionDTO dto) {
        BorrowingTransaction transaction = new BorrowingTransaction();
        transaction.setBookId(dto.getBookId());
        transaction.setMemberId(dto.getMemberId());
        transaction.setBorrowDate(dto.getBorrowDate());
        transaction.setDueDate(dto.getDueDate());
        transaction.setReturnDate(dto.getReturnDate());
        if (dto.getStatus() != null) {
            transaction.setStatus(dto.getStatus());
        }
        return transaction;
    }
}
