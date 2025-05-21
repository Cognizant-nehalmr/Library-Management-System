package com.example.lms.controller;

import com.example.lms.entity.Fine;
import com.example.lms.service.FineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/fines")
public class FineController {

    @Autowired
    private FineService fineService;

    @GetMapping
    public List<Fine> getAllFines() {
        return fineService.getAllFines();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Fine> getFineById(@PathVariable Long id) {
        Optional<Fine> fine = fineService.getFineById(id);
        return fine.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public Fine createFine(@RequestBody Fine fine) {
        return fineService.createFine(fine);
    }

    @PostMapping("/pay/{id}")
    public ResponseEntity<Fine> payFine(@PathVariable Long id) {
        try {
            Fine fine = fineService.payFine(id);
            return ResponseEntity.ok(fine);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Fine> updateFine(@PathVariable Long id, @RequestBody Fine fine) {
        Optional<Fine> updatedFine = fineService.updateFine(id, fine);
        return updatedFine.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/member/{memberId}")
    public List<Fine> getFinesByMember(@PathVariable Long memberId) {
        return fineService.getFinesByMember(memberId);
    }
}