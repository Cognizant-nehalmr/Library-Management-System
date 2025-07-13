package com.library.member.controller;

import com.library.member.dto.MemberDTO;
import com.library.member.service.MemberService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import java.util.Optional;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(MemberController.class)
class MemberControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private MemberService memberService;

    @Test
    void getMemberById_ReturnsMember() throws Exception {
        MemberDTO member = new MemberDTO();
        member.setMemberId(1L);
        member.setName("Test Member");
        Mockito.when(memberService.getMemberById(1L)).thenReturn(Optional.of(member));
        mockMvc.perform(get("/api/members/1"))
                .andExpect(status().isOk());
    }

    @Test
    void createMember_ReturnsCreated() throws Exception {
        MemberDTO member = new MemberDTO();
        member.setName("New Member");
        member.setEmail("test@email.com");
        Mockito.when(memberService.createMember(Mockito.any())).thenReturn(member);
        mockMvc.perform(post("/api/members")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\":\"New Member\",\"email\":\"test@email.com\"}"))
                .andExpect(status().isCreated());
    }

    @Test
    void updateMember_ReturnsOk() throws Exception {
        MemberDTO member = new MemberDTO();
        member.setMemberId(1L);
        member.setName("Updated");
        member.setEmail("updated@email.com");
        Mockito.when(memberService.updateMember(anyLong(), Mockito.any())).thenReturn(Optional.of(member));
        mockMvc.perform(put("/api/members/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\":\"Updated\",\"email\":\"updated@email.com\"}"))
                .andExpect(status().isOk());
    }

    @Test
    void deleteMember_ReturnsNoContent() throws Exception {
        Mockito.when(memberService.deleteMember(1L)).thenReturn(true);
        mockMvc.perform(delete("/api/members/1"))
                .andExpect(status().isNoContent());
    }
}
