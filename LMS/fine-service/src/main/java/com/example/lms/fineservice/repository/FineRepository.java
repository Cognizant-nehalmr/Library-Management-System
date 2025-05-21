package com.example.lms.fineservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.lms.fineservice.entity.Fine;

public interface FineRepository extends JpaRepository<Fine, Long> {
    // Additional query methods can be added here
}