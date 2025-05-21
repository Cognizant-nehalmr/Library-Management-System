package com.example.lms.memberservice.service;

import com.example.lms.memberservice.entity.Member;
import com.example.lms.memberservice.repository.MemberRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class MemberServiceTest {

    @InjectMocks
    private MemberService memberService;

    @Mock
    private MemberRepository memberRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllMembers() {
        // Arrange
        Member member1 = new Member();
        member1.setId(1L);
        member1.setName("John Doe");

        Member member2 = new Member();
        member2.setId(2L);
        member2.setName("Jane Doe");

        when(memberRepository.findAll()).thenReturn(Arrays.asList(member1, member2));

        // Act
        List<Member> members = memberService.getAllMembers();

        // Assert
        assertEquals(2, members.size());
        verify(memberRepository, times(1)).findAll();
    }

    @Test
    void testAddMember() {
        // Arrange
        Member member = new Member();
        member.setId(1L);
        member.setName("John Doe");

        when(memberRepository.save(member)).thenReturn(member);

        // Act
        Member savedMember = memberService.addMember(member);

        // Assert
        assertEquals("John Doe", savedMember.getName());
        verify(memberRepository, times(1)).save(member);
    }
}