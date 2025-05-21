package com.example.lms.fineservice.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.lms.fineservice.repository.FineRepository;
import com.example.lms.fineservice.entity.Fine;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class FineService {

    private static final Logger logger = LoggerFactory.getLogger(FineService.class);

    @Autowired
    private FineRepository fineRepository;

    public List<Fine> getAllFines() {
        logger.info("Fetching all fines");
        return fineRepository.findAll();
    }

    public Fine addFine(Fine fine) {
        logger.info("Adding a new fine for member ID: {}", fine.getMemberId());
        return fineRepository.save(fine);
    }

    // Additional service methods can be added here
}