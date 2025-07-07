package com.library.fine.service;

import com.library.fine.client.TransactionServiceClient;
import com.library.fine.dto.BorrowingTransactionResponseDTO;
import com.library.fine.dto.FineDTO;
import com.library.fine.dto.FineResponseDTO;
import com.library.fine.entity.Fine;
import com.library.fine.repository.FineRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

@Service
@Transactional
public class FineService {

    @Autowired
    private FineRepository fineRepository;

    @Autowired
    private TransactionServiceClient transactionServiceClient;

    private static final BigDecimal DAILY_FINE_RATE = new BigDecimal("10.0"); // Rs.10 per day



    public List<FineResponseDTO> getAllFines() {
        return fineRepository.findAll().stream()
                .map(fine -> {
                    BorrowingTransactionResponseDTO transaction = transactionServiceClient.getTransactionById(fine.getTransactionId());
                    return new FineResponseDTO(convertToDTO(fine), transaction);
                })
                .collect(Collectors.toList());
    }


    public Optional<FineResponseDTO> getFineById(Long id) {
        return fineRepository.findById(id)
                .map(fine -> {
                    BorrowingTransactionResponseDTO transaction = transactionServiceClient.getTransactionById(fine.getTransactionId());
                    return new FineResponseDTO(convertToDTO(fine), transaction);
                });

    }

    public List<FineResponseDTO> getFinesByMemberId(Long memberId) {
        return fineRepository.findByMemberId(memberId).stream()
                .map(fine -> {
                    BorrowingTransactionResponseDTO transaction = transactionServiceClient.getTransactionById(fine.getTransactionId());
                    return new FineResponseDTO(convertToDTO(fine), transaction);
                })
                .collect(Collectors.toList());
    }

    public List<FineResponseDTO> getPendingFines() {
        return fineRepository.findByStatus(Fine.FineStatus.PENDING).stream()
                .map(fine -> {
                    BorrowingTransactionResponseDTO transaction = transactionServiceClient.getTransactionById(fine.getTransactionId());
                    return new FineResponseDTO(convertToDTO(fine), transaction);
                })
                .collect(Collectors.toList());
    }

    public BigDecimal getTotalPendingFinesByMember(Long memberId) {
        return fineRepository.getTotalPendingFinesByMember(memberId);
    }

    public FineResponseDTO createFine(Long transactionId) {
        // Fetch transaction details using Feign Client
        BorrowingTransactionResponseDTO transaction = transactionServiceClient.getTransactionById(transactionId);

        // Check if fine already exists for this transaction
        if (fineRepository.existsByTransactionId(transactionId)) {
            throw new RuntimeException("Fine already exists for this transaction");
        }

        // Calculate overdue days
        LocalDate overdueDate = transaction.getDueDate();
        LocalDate currentDate = LocalDate.now();
        int overdueDays = (int) ChronoUnit.DAYS.between(overdueDate, currentDate);

        // Get member ID from transaction
        Long memberId = transaction.getMember().getMemberId();

        // Calculate fine amount
        BigDecimal amount = DAILY_FINE_RATE.multiply(new BigDecimal(overdueDays));
        Fine fine = new Fine(memberId, transactionId, amount);
        Fine savedFine = fineRepository.save(fine);

        return new FineResponseDTO(convertToDTO(savedFine), transaction);
    }


    public Optional<FineResponseDTO> payFine(Long fineId) {
        return fineRepository.findById(fineId)
                .map(fine -> {
                    if (fine.getStatus() == Fine.FineStatus.PAID) {
                        throw new RuntimeException("Fine is already paid");
                    }
                    fine.setStatus(Fine.FineStatus.PAID);
                    fine.setPaidDate(LocalDateTime.now());
                    Fine updatedFine = fineRepository.save(fine);
                    return new FineResponseDTO(convertToDTO(updatedFine), transactionServiceClient.getTransactionById(fine.getTransactionId()));
                });
    }

    public boolean deleteFine(Long id) {
        if (fineRepository.existsById(id)) {
            fineRepository.deleteById(id);
            return true;
        }
        return false;
    }

    @Scheduled(cron = "0 0 1 * * ?") // Run daily at 1 AM
    public String processOverdueFines() {
        transactionServiceClient.updateOverdueTransactions();
        // Updates Transactions If They Are Overdue
        List<BorrowingTransactionResponseDTO> allTransactions = transactionServiceClient.getAllTransactions();

        // Create fines for overdue transactions
        allTransactions.forEach(transaction -> {
            try {
                if (transaction.getStatus().equals("OVERDUE")){
                    createFine(transaction.getTransactionId());
                }
            } catch (RuntimeException e) {
                System.err.println("Failed to create fine for transaction ID " + transaction.getTransactionId() + ": " + e.getMessage());
            }
        });

        System.out.println("Processing overdue fines at: " + LocalDateTime.now());
        return "Processing overdue fines at: " + LocalDateTime.now();
    }



    private FineDTO convertToDTO(Fine fine) {
        FineDTO dto = new FineDTO();
        dto.setFineId(fine.getFineId());
        dto.setMemberId(fine.getMemberId());
        dto.setTransactionId(fine.getTransactionId());
        dto.setAmount(fine.getAmount());
        dto.setStatus(fine.getStatus());
        dto.setTransactionDate(fine.getTransactionDate());
        dto.setPaidDate(fine.getPaidDate());
        return dto;
    }
}
