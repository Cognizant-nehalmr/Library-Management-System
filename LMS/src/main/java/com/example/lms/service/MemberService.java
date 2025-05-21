package com.example.lms.service;

import com.example.lms.entity.Member;
import com.example.lms.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class MemberService {

    @Autowired
    private MemberRepository memberRepository;

    public List<Member> getAllMembers() {
        return memberRepository.findAll();
    }

    public Optional<Member> getMemberById(Long id) {
        return memberRepository.findById(id);
    }

    public Member saveMember(Member member) {
        return memberRepository.save(member);
    }

    public void deleteMember(Long id) {
        memberRepository.deleteById(id);
    }

    // Example usage of Member fields in service logic
    public List<String> getMemberDetails() {
        return memberRepository.findAll().stream()
                .map(member -> "Name: " + member.getName() + ", Email: " + member.getEmail() + ", Phone: " + member.getPhone())
                .collect(Collectors.toList());
    }

    // Directly access fields in a method to ensure they are utilized
    public void printMemberDetails() {
        memberRepository.findAll().forEach(member -> {
            System.out.println("Member ID: " + member.getMemberId());
            System.out.println("Name: " + member.getName());
            System.out.println("Email: " + member.getEmail());
            System.out.println("Phone: " + member.getPhone());
            System.out.println("Address: " + member.getAddress());
            System.out.println("Membership Status: " + member.getMembershipStatus());
        });
    }

    public Optional<Member> updateMember(Long id, Member member) {
        return memberRepository.findById(id).map(existingMember -> {
            existingMember.setName(member.getName());
            existingMember.setEmail(member.getEmail());
            existingMember.setPhone(member.getPhone());
            existingMember.setAddress(member.getAddress());
            existingMember.setMembershipStatus(member.getMembershipStatus());
            return memberRepository.save(existingMember);
        });
    }

    public List<Member> getMembersByStatus(String status) {
        return memberRepository.findAll().stream()
                .filter(member -> member.getMembershipStatus().name().equalsIgnoreCase(status))
                .collect(Collectors.toList());
    }
}