package com.library.member.service;

import com.library.member.dto.MemberDTO;
import com.library.member.entity.Member;
import com.library.member.repository.MemberRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import java.util.Optional;
import java.util.Collections;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class MemberServiceTest {
    @Mock
    private MemberRepository memberRepository;
    @InjectMocks
    private MemberService memberService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetMemberById_Success() {
        Member member = new Member();
        member.setMemberId(1L);
        member.setName("Test Member");
        when(memberRepository.findById(1L)).thenReturn(Optional.of(member));
        Optional<MemberDTO> result = memberService.getMemberById(1L);
        assertTrue(result.isPresent());
        assertEquals(1L, result.get().getMemberId());
        assertEquals("Test Member", result.get().getName());
    }

    @Test
    void testGetMemberById_NotFound() {
        when(memberRepository.findById(2L)).thenReturn(Optional.empty());
        Optional<MemberDTO> result = memberService.getMemberById(2L);
        assertFalse(result.isPresent());
    }

    @Test
    void testGetAllMembers_Empty() {
        when(memberRepository.findAll()).thenReturn(Collections.emptyList());
        List<MemberDTO> result = memberService.getAllMembers();
        assertTrue(result.isEmpty());
    }

    @Test
    void testCreateMember() {
        MemberDTO dto = new MemberDTO();
        dto.setName("New Member");
        dto.setEmail("test@email.com");
        when(memberRepository.existsByEmail(dto.getEmail())).thenReturn(false);
        Member member = new Member();
        member.setMemberId(10L);
        member.setName("New Member");
        when(memberRepository.save(any(Member.class))).thenReturn(member);
        MemberDTO saved = memberService.createMember(dto);
        assertNotNull(saved);
        assertEquals("New Member", saved.getName());
    }

    @Test
    void testCreateMember_DuplicateEmail() {
        MemberDTO dto = new MemberDTO();
        dto.setEmail("duplicate@email.com");
        when(memberRepository.existsByEmail(dto.getEmail())).thenReturn(true);
        assertThrows(RuntimeException.class, () -> memberService.createMember(dto));
    }

    @Test
    void testUpdateMember_Success() {
        Member existing = new Member();
        existing.setMemberId(1L);
        existing.setEmail("old@email.com");
        MemberDTO update = new MemberDTO();
        update.setEmail("old@email.com");
        update.setName("Updated");
        when(memberRepository.findById(1L)).thenReturn(Optional.of(existing));
        when(memberRepository.save(any(Member.class))).thenReturn(existing);
        Optional<MemberDTO> result = memberService.updateMember(1L, update);
        assertTrue(result.isPresent());
        assertEquals("Updated", result.get().getName());
    }

    @Test
    void testUpdateMember_NotFound() {
        MemberDTO update = new MemberDTO();
        when(memberRepository.findById(99L)).thenReturn(Optional.empty());
        Optional<MemberDTO> result = memberService.updateMember(99L, update);
        assertFalse(result.isPresent());
    }

    @Test
    void testDeleteMember_Success() {
        when(memberRepository.existsById(1L)).thenReturn(true);
        doNothing().when(memberRepository).deleteById(1L);
        assertTrue(memberService.deleteMember(1L));
    }

    @Test
    void testDeleteMember_NotFound() {
        when(memberRepository.existsById(2L)).thenReturn(false);
        assertFalse(memberService.deleteMember(2L));
    }

    @Test
    void testUpdateMembershipStatus_Success() {
        Member member = new Member();
        member.setMemberId(1L);
        when(memberRepository.findById(1L)).thenReturn(Optional.of(member));
        when(memberRepository.save(any(Member.class))).thenReturn(member);
        Optional<MemberDTO> result = memberService.updateMembershipStatus(1L, Member.MembershipStatus.ACTIVE);
        assertTrue(result.isPresent());
        assertEquals(Member.MembershipStatus.ACTIVE, result.get().getMembershipStatus());
    }

    @Test
    void testUpdateMembershipStatus_NotFound() {
        when(memberRepository.findById(2L)).thenReturn(Optional.empty());
        Optional<MemberDTO> result = memberService.updateMembershipStatus(2L, Member.MembershipStatus.ACTIVE);
        assertFalse(result.isPresent());
    }
}
