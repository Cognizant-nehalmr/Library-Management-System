package com.library.fine.service;

import com.library.fine.dto.FineDTO;
import com.library.fine.entity.Fine;
import com.library.fine.repository.FineRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class FineService {

    @Autowired
    private FineRepository fineRepository;

    private static final BigDecimal DAILY_FINE_RATE = new BigDecimal("1.00"); // $1 per day

    public List<FineDTO> getAllFines() {
        return fineRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public Optional<FineDTO> getFineById(Long id) {
        return fineRepository.findById(id)
                .map(this::convertToDTO);
    }

    public List<FineDTO> getFinesByMemberId(Long memberId) {
        return fineRepository.findByMemberId(memberId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<FineDTO> getPendingFines() {
        return fineRepository.findByStatus(Fine.FineStatus.PENDING).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public BigDecimal getTotalPendingFinesByMember(Long memberId) {
        return fineRepository.getTotalPendingFinesByMember(memberId);
    }

    public FineDTO createFine(Long memberId, Long transactionId, int overdueDays) {
        // Check if fine already exists for this transaction
        if (fineRepository.existsByTransactionId(transactionId)) {
            throw new RuntimeException("Fine already exists for this transaction");
        }

        BigDecimal amount = DAILY_FINE_RATE.multiply(new BigDecimal(overdueDays));
        Fine fine = new Fine(memberId, transactionId, amount);
        Fine savedFine = fineRepository.save(fine);
        return convertToDTO(savedFine);
    }

    public Optional<FineDTO> payFine(Long fineId) {
        return fineRepository.findById(fineId)
                .map(fine -> {
                    if (fine.getStatus() == Fine.FineStatus.PAID) {
                        throw new RuntimeException("Fine is already paid");
                    }
                    
                    fine.setStatus(Fine.FineStatus.PAID);
                    fine.setPaidDate(LocalDateTime.now());
                    Fine updatedFine = fineRepository.save(fine);
                    return convertToDTO(updatedFine);
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
    public void processOverdueFines() {
        // This would typically integrate with transaction service
        // to get overdue transactions and create fines
        System.out.println("Processing overdue fines at: " + LocalDateTime.now());
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
