package com.example.lms.service;

import com.example.lms.entity.Fine;
import com.example.lms.repository.FineRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class FineService {

    @Autowired
    private FineRepository fineRepository;

    public List<Fine> getAllFines() {
        return fineRepository.findAll();
    }

    public Optional<Fine> getFineById(Long id) {
        return fineRepository.findById(id);
    }

    public Fine createFine(Fine fine) {
        fine.setTransactionDate(LocalDate.now());
        fine.setStatus(Fine.Status.PENDING);
        return fineRepository.save(fine);
    }

    public Fine payFine(Long fineId) {
        Optional<Fine> fineOpt = fineRepository.findById(fineId);
        if (fineOpt.isPresent()) {
            Fine fine = fineOpt.get();
            fine.setStatus(Fine.Status.PAID);
            return fineRepository.save(fine);
        }
        throw new IllegalArgumentException("Fine not found");
    }

    public Optional<Fine> updateFine(Long id, Fine fine) {
        return fineRepository.findById(id).map(existingFine -> {
            existingFine.setAmount(fine.getAmount());
            existingFine.setStatus(fine.getStatus());
            existingFine.setTransactionDate(fine.getTransactionDate());
            return fineRepository.save(existingFine);
        });
    }

    public List<Fine> getFinesByMember(Long memberId) {
        return fineRepository.findAll().stream()
                .filter(fine -> fine.getMember().getMemberId().equals(memberId))
                .collect(Collectors.toList());
    }
}