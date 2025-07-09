package com.library.fine.controller;

import com.library.fine.dto.FineResponseDTO;
import com.library.fine.entity.Fine.FineType;
import com.library.fine.service.FineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/fines")
@CrossOrigin(origins = "*")
public class FineController {

    @Autowired
    private FineService fineService;

    @GetMapping
    public ResponseEntity<List<FineResponseDTO>> getAllFines() {
        return ResponseEntity.ok(fineService.getAllFines());
    }

    @GetMapping("/collected")
    public ResponseEntity<BigDecimal> getTotalCollectedFines() {
        return ResponseEntity.ok(fineService.getTotalCollectedFines());
    }

    @GetMapping("/pending")
    public ResponseEntity<BigDecimal> getTotalPendingFines() {
        return ResponseEntity.ok(fineService.getTotalPendingFines());
    }

    @GetMapping("/{id}")
    public ResponseEntity<FineResponseDTO> getFineById(@PathVariable Long id) {
        return fineService.getFineById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/member/{memberId}")
    public ResponseEntity<List<FineResponseDTO>> getFinesByMemberId(@PathVariable Long memberId) {
        return ResponseEntity.ok(fineService.getFinesByMemberId(memberId));
    }

    @GetMapping("/member/{memberId}/total")
    public ResponseEntity<Map<String, BigDecimal>> getTotalPendingFinesByMember(@PathVariable Long memberId) {
        BigDecimal total = fineService.getTotalPendingFinesByMember(memberId);
        return ResponseEntity.ok(Map.of("totalPendingFines", total));
    }

    @PostMapping("/{transactionId}/{fineType}")
    public ResponseEntity<FineResponseDTO> createFine(
            @PathVariable Long transactionId,
            @PathVariable FineType fineType,
            @RequestParam(required = false) BigDecimal amount) {
        FineResponseDTO fine = fineService.createFine(transactionId, fineType, amount);
        return ResponseEntity.ok(fine);
    }

    @PutMapping("/{id}/pay")
    public ResponseEntity<?> payFine(@PathVariable Long id) {
        try {
            return fineService.payFine(id)
                    .map(ResponseEntity::ok)
                    .orElse(ResponseEntity.notFound().build());
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @PutMapping("/{id}/reverse")
    public ResponseEntity<?> reverseFinePayment(@PathVariable Long id) {
        try {
            return fineService.reverseFinePayment(id)
                    .map(ResponseEntity::ok)
                    .orElse(ResponseEntity.notFound().build());
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @PutMapping("/{id}/cancel")
    public ResponseEntity<?> cancelFine(@PathVariable Long id) {
        try {
            return fineService.cancelFine(id)
                    .map(ResponseEntity::ok)
                    .orElse(ResponseEntity.notFound().build());
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @PutMapping("/update-fines")
    public ResponseEntity<String> updateFines() {
        try {
            return ResponseEntity.ok(fineService.processOverdueFines());
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()).toString());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFine(@PathVariable Long id) {
        if (fineService.deleteFine(id)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}
