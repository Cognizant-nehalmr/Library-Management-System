package com.example.lms.fineservice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.example.lms.fineservice.service.FineService;
import com.example.lms.fineservice.entity.Fine;
import java.util.List;

@RestController
@RequestMapping("/fines")
public class FineController {

    @Autowired
    private FineService fineService;

    @GetMapping
    public List<Fine> getAllFines() {
        return fineService.getAllFines();
    }

    @PostMapping
    public Fine addFine(@RequestBody Fine fine) {
        return fineService.addFine(fine);
    }

    // Additional endpoints can be added here
}