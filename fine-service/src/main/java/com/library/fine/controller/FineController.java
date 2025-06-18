package com.library.fine.controller;

import com.library.fine.dto.FineDTO;
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
    public ResponseEntity<List<FineDTO>> getAllFines() {
        List<FineDTO> fines = fineService.getAllFines();
        return ResponseEntity.ok(fines);
    }

    @GetMapping("/{id}")
    public ResponseEntity<FineDTO> getFineById(@PathVariable Long id) {
        return fineService.getFineById(id)
                .map(fine -> ResponseEntity.ok(fine))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/member/{memberId}")
    public ResponseEntity<List<FineDTO>> getFinesByMemberId(@PathVariable Long memberId) {
        List<FineDTO> fines = fineService.getFinesByMemberId(memberId);
        return ResponseEntity.ok(fines);
    }

    @GetMapping("/pending")
    public ResponseEntity<List<FineDTO>> getPendingFines() {
        List<FineDTO> fines = fineService.getPendingFines();
        return ResponseEntity.ok(fines);
    }

    @GetMapping("/member/{memberId}/total")
    public ResponseEntity<Map<String, BigDecimal>> getTotalPendingFinesByMember(@PathVariable Long memberId) {
        BigDecimal total = fineService.getTotalPendingFinesByMember(memberId);
        return ResponseEntity.ok(Map.of("totalPendingFines", total));
    }

    @PostMapping
    public ResponseEntity<?> createFine(@RequestBody Map<String, Object> request) {
        try {
            Long memberId = Long.valueOf(request.get("memberId").toString());
            Long transactionId = Long.valueOf(request.get("transactionId").toString());
            Integer overdueDays = Integer.valueOf(request.get("overdueDays").toString());
            
            FineDTO createdFine = fineService.createFine(memberId, transactionId, overdueDays);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdFine);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @PutMapping("/{id}/pay")
    public ResponseEntity<?> payFine(@PathVariable Long id) {
        try {
            return fineService.payFine(id)
                    .map(fine -> ResponseEntity.ok(fine))
                    .orElse(ResponseEntity.notFound().build());
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
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
