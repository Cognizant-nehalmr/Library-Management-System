package com.example.lms.memberservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.lms.memberservice.entity.Member;

public interface MemberRepository extends JpaRepository<Member, Long> {
    // Additional query methods can be added here
}