package com.example.lms.service;

import com.example.lms.entity.Member;
import com.example.lms.repository.MemberRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

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
    void testGetAllMembers() {
        List<Member> members = List.of(new Member());
        when(memberRepository.findAll()).thenReturn(members);

        List<Member> result = memberService.getAllMembers();

        assertEquals(1, result.size());
        verify(memberRepository, times(1)).findAll();
    }

    @Test
    void testSaveMember() {
        Member member = new Member();
        when(memberRepository.save(member)).thenReturn(member);

        Member result = memberService.saveMember(member);

        assertNotNull(result);
        verify(memberRepository, times(1)).save(member);
    }
}