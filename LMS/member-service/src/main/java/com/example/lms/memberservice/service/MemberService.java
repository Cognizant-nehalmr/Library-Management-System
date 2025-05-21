package com.example.lms.memberservice.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.lms.memberservice.repository.MemberRepository;
import com.example.lms.memberservice.entity.Member;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class MemberService {

    private static final Logger logger = LoggerFactory.getLogger(MemberService.class);

    @Autowired
    private MemberRepository memberRepository;

    public List<Member> getAllMembers() {
        logger.info("Fetching all members");
        return memberRepository.findAll();
    }

    public Member addMember(Member member) {
        logger.info("Adding a new member: {}", member.getName());
        return memberRepository.save(member);
    }

    // Additional service methods can be added here
}