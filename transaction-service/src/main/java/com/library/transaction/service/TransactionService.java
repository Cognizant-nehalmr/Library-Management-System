package com.library.transaction.service;

import com.library.transaction.client.*;
import com.library.transaction.dto.*;
import com.library.transaction.entity.BorrowingTransaction;
import com.library.transaction.repository.BorrowingTransactionRepository;

import feign.FeignException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
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

    @Autowired
    private MemberServiceClient memberServiceClient;

    public List<BorrowingTransactionResponseDTO> getAllTransactions() {
        return transactionRepository.findAll().stream()
                .map(transaction -> {
                    BorrowingTransactionResponseDTO responseDTO = mapToResponseDTO(transaction);

                    // Fetch Book
                    try {
                        ResponseEntity<BookDTO> bookResponse = bookServiceClient.getBookById(transaction.getBookId());
                        responseDTO.setBook(bookResponse.getBody());
                    } catch (FeignException.NotFound e) {
                        throw new RuntimeException("Book not found for ID: " + transaction.getBookId());
                    }

                    // Fetch Member
                    try {
                        ResponseEntity<MemberDTO> memberResponse = memberServiceClient
                                .getMemberById(transaction.getMemberId());
                        responseDTO.setMember(memberResponse.getBody());
                    } catch (FeignException.NotFound e) {
                        throw new RuntimeException("Member not found for ID: " + transaction.getMemberId());
                    }

                    return responseDTO;
                })
                .collect(Collectors.toList());
    }

    public Optional<BorrowingTransactionResponseDTO> getTransactionById(Long id) {
        return transactionRepository.findById(id)
                .map(transaction -> {
                    BorrowingTransactionResponseDTO responseDTO = mapToResponseDTO(transaction);

                    // Fetch Book
                    try {
                        ResponseEntity<BookDTO> bookResponse = bookServiceClient.getBookById(transaction.getBookId());
                        responseDTO.setBook(bookResponse.getBody());
                    } catch (FeignException.NotFound e) {
                        throw new RuntimeException("Book not found with ID: " + transaction.getBookId());
                    }

                    // Fetch Member
                    try {
                        ResponseEntity<MemberDTO> memberResponse = memberServiceClient
                                .getMemberById(transaction.getMemberId());
                        responseDTO.setMember(memberResponse.getBody());
                    } catch (FeignException.NotFound e) {
                        throw new RuntimeException("Member not found with ID: " + transaction.getMemberId());
                    }

                    return responseDTO;
                });
    }

    public List<BorrowingTransactionResponseDTO> getTransactionsByMemberId(Long memberId) {
        return transactionRepository.findByMemberId(memberId).stream()
                .map(transaction -> {
                    BorrowingTransactionResponseDTO responseDTO = mapToResponseDTO(transaction);

                    // Fetch Book
                    try {
                        ResponseEntity<BookDTO> bookResponse = bookServiceClient.getBookById(transaction.getBookId());
                        responseDTO.setBook(bookResponse.getBody());
                    } catch (FeignException.NotFound e) {
                        throw new RuntimeException("Book not found with ID: " + transaction.getBookId());
                    }

                    // Fetch Member (you already know memberId, but still include the full object)
                    try {
                        ResponseEntity<MemberDTO> memberResponse = memberServiceClient
                                .getMemberById(transaction.getMemberId());
                        responseDTO.setMember(memberResponse.getBody());
                    } catch (FeignException.NotFound e) {
                        throw new RuntimeException("Member not found with ID: " + transaction.getMemberId());
                    }

                    return responseDTO;
                })
                .collect(Collectors.toList());
    }

    public List<BorrowingTransactionResponseDTO> getTransactionsByBookId(Long bookId) {
        return transactionRepository.findByBookId(bookId).stream()
                .map(transaction -> {
                    BorrowingTransactionResponseDTO responseDTO = mapToResponseDTO(transaction);

                    // ✅ Fetch Book
                    try {
                        ResponseEntity<BookDTO> bookResponse = bookServiceClient.getBookById(transaction.getBookId());
                        responseDTO.setBook(bookResponse.getBody());
                    } catch (FeignException.NotFound e) {
                        throw new RuntimeException("Book not found with ID: " + transaction.getBookId());
                    }

                    // ✅ Fetch Member
                    try {
                        ResponseEntity<MemberDTO> memberResponse = memberServiceClient
                                .getMemberById(transaction.getMemberId());
                        responseDTO.setMember(memberResponse.getBody());
                    } catch (FeignException.NotFound e) {
                        throw new RuntimeException("Member not found with ID: " + transaction.getMemberId());
                    }

                    return responseDTO;
                })
                .collect(Collectors.toList());
    }

    public List<BorrowingTransactionResponseDTO> getOverdueTransactions() {
        LocalDate today = LocalDate.now();

        return transactionRepository.findOverdueTransactions(today).stream()
                .map(transaction -> {
                    BorrowingTransactionResponseDTO responseDTO = mapToResponseDTO(transaction);

                    // ✅ Fetch Book
                    try {
                        ResponseEntity<BookDTO> bookResponse = bookServiceClient.getBookById(transaction.getBookId());
                        responseDTO.setBook(bookResponse.getBody());
                    } catch (FeignException.NotFound e) {
                        throw new RuntimeException("Book not found with ID: " + transaction.getBookId());
                    }

                    // ✅ Fetch Member
                    try {
                        ResponseEntity<MemberDTO> memberResponse = memberServiceClient
                                .getMemberById(transaction.getMemberId());
                        responseDTO.setMember(memberResponse.getBody());
                    } catch (FeignException.NotFound e) {
                        throw new RuntimeException("Member not found with ID: " + transaction.getMemberId());
                    }

                    return responseDTO;
                })
                .collect(Collectors.toList());
    }

    public BorrowingTransactionResponseDTO borrowBook(BorrowingTransactionDTO transactionDTO) {
        // ✅ Validate Member borrowing limit
        long activeBorrowings = transactionRepository.countByMemberIdAndStatus(
                transactionDTO.getMemberId(), BorrowingTransaction.TransactionStatus.BORROWED);
        if (activeBorrowings >= 5) {
            throw new RuntimeException("Member has reached maximum borrowing limit.");
        }

        // ✅ Fetch Book by ID
        BookDTO book;
        try {
            ResponseEntity<BookDTO> bookResponse = bookServiceClient.getBookById(transactionDTO.getBookId());
            book = bookResponse.getBody();
            if (book == null) {
                throw new RuntimeException("Book not found with ID: " + transactionDTO.getBookId());
            }
            if (book.getAvailableCopies() == 0) {
                throw new RuntimeException("No available copies for Book ID: " + transactionDTO.getBookId());
            }
        } catch (FeignException.NotFound e) {
            throw new RuntimeException("Book not found with ID: " + transactionDTO.getBookId());
        }

        // ✅ Fetch Member by ID
        MemberDTO member;
        try {
            ResponseEntity<MemberDTO> memberResponse = memberServiceClient.getMemberById(transactionDTO.getMemberId());
            member = memberResponse.getBody();
            if (member == null) {
                throw new RuntimeException("Member not found with ID: " + transactionDTO.getMemberId());
            }
        } catch (FeignException.NotFound e) {
            throw new RuntimeException("Member not found with ID: " + transactionDTO.getMemberId());
        }

        // ✅ Update book availability
        try {
            bookServiceClient.updateBookAvailability(transactionDTO.getBookId(), Map.of("change", -1));
        } catch (Exception e) {
            throw new RuntimeException("Unable to update book availability: " + e.getMessage());
        }

        // ✅ Save Transaction
        BorrowingTransaction transaction = convertToEntity(transactionDTO);
        transaction.setBorrowDate(LocalDate.now());
        transaction.setDueDate(transaction.getDueDate() != null ? transaction.getDueDate() : LocalDate.now().plusDays(14));
        transaction.setStatus(BorrowingTransaction.TransactionStatus.BORROWED);
        BorrowingTransaction savedTransaction = transactionRepository.save(transaction);

        // ✅ Return combined response
        return new BorrowingTransactionResponseDTO(savedTransaction, book, member);
    }

    public Optional<BorrowingTransactionResponseDTO> returnBook(Long transactionId) {
        return transactionRepository.findById(transactionId)
                .map(transaction -> {
                    if (transaction.getStatus() != BorrowingTransaction.TransactionStatus.BORROWED &&
                            transaction.getStatus() != BorrowingTransaction.TransactionStatus.OVERDUE) {
                        throw new RuntimeException("Book is not currently borrowed");
                    }

                    // ✅ Update book availability
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

                    // ✅ Prepare response DTO
                    BorrowingTransactionResponseDTO responseDTO = mapToResponseDTO(transaction);

                    // ✅ Fetch and attach book
                    try {
                        ResponseEntity<BookDTO> bookResponse = bookServiceClient
                                .getBookById(updatedTransaction.getBookId());
                        responseDTO.setBook(bookResponse.getBody());
                    } catch (FeignException.NotFound e) {
                        throw new RuntimeException("Book not found with ID: " + updatedTransaction.getBookId());
                    }

                    // ✅ Fetch and attach member
                    try {
                        ResponseEntity<MemberDTO> memberResponse = memberServiceClient
                                .getMemberById(updatedTransaction.getMemberId());
                        responseDTO.setMember(memberResponse.getBody());
                    } catch (FeignException.NotFound e) {
                        throw new RuntimeException("Member not found with ID: " + updatedTransaction.getMemberId());
                    }

                    return responseDTO;
                });
    }

    @Scheduled(cron = "0 0 1 * * ?") // Every day at 1:00 AM
    public void updateOverdueTransactions() {
        LocalDate today = LocalDate.now();
        List<BorrowingTransaction> overdueTransactions = transactionRepository.findOverdueTransactions(today);

        for (BorrowingTransaction transaction : overdueTransactions) {
            if (transaction.getStatus() == BorrowingTransaction.TransactionStatus.BORROWED) {
                transaction.setStatus(BorrowingTransaction.TransactionStatus.OVERDUE);
                transactionRepository.save(transaction);
            }
        }

        System.out.println("✅ Overdue transactions updated at: " + LocalDateTime.now());
    }

    private BorrowingTransactionResponseDTO mapToResponseDTO(BorrowingTransaction transaction) {
        BorrowingTransactionResponseDTO dto = new BorrowingTransactionResponseDTO();
        dto.setTransactionId(transaction.getTransactionId());
        dto.setBorrowDate(transaction.getBorrowDate());
        dto.setDueDate(transaction.getDueDate());
        dto.setReturnDate(transaction.getReturnDate());
        dto.setStatus(transaction.getStatus().toString());
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
