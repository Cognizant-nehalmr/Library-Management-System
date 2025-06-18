package com.library.member.service;

import com.library.member.dto.MemberDTO;
import com.library.member.entity.Member;
import com.library.member.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class MemberService {

    @Autowired
    private MemberRepository memberRepository;

    public List<MemberDTO> getAllMembers() {
        return memberRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public Optional<MemberDTO> getMemberById(Long id) {
        return memberRepository.findById(id)
                .map(this::convertToDTO);
    }

    public Optional<MemberDTO> getMemberByEmail(String email) {
        return memberRepository.findByEmail(email)
                .map(this::convertToDTO);
    }

    public List<MemberDTO> getMembersByStatus(Member.MembershipStatus status) {
        return memberRepository.findByMembershipStatus(status).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<MemberDTO> searchMembersByName(String name) {
        return memberRepository.findByNameContainingIgnoreCase(name).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public MemberDTO createMember(MemberDTO memberDTO) {
        if (memberRepository.existsByEmail(memberDTO.getEmail())) {
            throw new RuntimeException("Member with email " + memberDTO.getEmail() + " already exists");
        }
        
        Member member = convertToEntity(memberDTO);
        Member savedMember = memberRepository.save(member);
        return convertToDTO(savedMember);
    }

    public Optional<MemberDTO> updateMember(Long id, MemberDTO memberDTO) {
        return memberRepository.findById(id)
                .map(existingMember -> {
                    // Check if email is being changed and if new email already exists
                    if (!existingMember.getEmail().equals(memberDTO.getEmail()) && 
                        memberRepository.existsByEmail(memberDTO.getEmail())) {
                        throw new RuntimeException("Member with email " + memberDTO.getEmail() + " already exists");
                    }
                    
                    updateMemberFields(existingMember, memberDTO);
                    Member updatedMember = memberRepository.save(existingMember);
                    return convertToDTO(updatedMember);
                });
    }

    public boolean deleteMember(Long id) {
        if (memberRepository.existsById(id)) {
            memberRepository.deleteById(id);
            return true;
        }
        return false;
    }

    public Optional<MemberDTO> updateMembershipStatus(Long id, Member.MembershipStatus status) {
        return memberRepository.findById(id)
                .map(member -> {
                    member.setMembershipStatus(status);
                    Member updatedMember = memberRepository.save(member);
                    return convertToDTO(updatedMember);
                });
    }

    private MemberDTO convertToDTO(Member member) {
        MemberDTO dto = new MemberDTO();
        dto.setMemberId(member.getMemberId());
        dto.setName(member.getName());
        dto.setEmail(member.getEmail());
        dto.setPhone(member.getPhone());
        dto.setAddress(member.getAddress());
        dto.setMembershipStatus(member.getMembershipStatus());
        return dto;
    }

    private Member convertToEntity(MemberDTO dto) {
        Member member = new Member();
        member.setName(dto.getName());
        member.setEmail(dto.getEmail());
        member.setPhone(dto.getPhone());
        member.setAddress(dto.getAddress());
        if (dto.getMembershipStatus() != null) {
            member.setMembershipStatus(dto.getMembershipStatus());
        }
        return member;
    }

    private void updateMemberFields(Member member, MemberDTO dto) {
        member.setName(dto.getName());
        member.setEmail(dto.getEmail());
        member.setPhone(dto.getPhone());
        member.setAddress(dto.getAddress());
        if (dto.getMembershipStatus() != null) {
            member.setMembershipStatus(dto.getMembershipStatus());
        }
    }
}
