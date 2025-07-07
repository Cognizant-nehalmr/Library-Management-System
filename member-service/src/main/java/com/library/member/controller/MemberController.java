package com.library.member.controller;

import com.library.member.dto.MemberDTO;
import com.library.member.entity.Member;
import com.library.member.service.MemberService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/members")
@CrossOrigin(origins = "*")
public class MemberController {

    @Autowired
    private MemberService memberService;

    @GetMapping
    public ResponseEntity<List<MemberDTO>> getAllMembers() {
        List<MemberDTO> members = memberService.getAllMembers();
        return ResponseEntity.ok(members);
    }

    @GetMapping("/{id}")
    public ResponseEntity<MemberDTO> getMemberById(@PathVariable Long id) {
        return memberService.getMemberById(id)
                .map(member -> ResponseEntity.ok(member))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/email/{email}")
    public ResponseEntity<MemberDTO> getMemberByEmail(@PathVariable String email) {
        return memberService.getMemberByEmail(email)
                .map(member -> ResponseEntity.ok(member))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<MemberDTO>> getMembersByStatus(@PathVariable Member.MembershipStatus status) {
        List<MemberDTO> members = memberService.getMembersByStatus(status);
        return ResponseEntity.ok(members);
    }

    @GetMapping("/search")
    public ResponseEntity<List<MemberDTO>> searchMembersByName(@RequestParam String name) {
        List<MemberDTO> members = memberService.searchMembersByName(name);
        return ResponseEntity.ok(members);
    }

    @PostMapping
    public ResponseEntity<?> createMember(@Valid @RequestBody MemberDTO memberDTO) {
        try {
            MemberDTO createdMember = memberService.createMember(memberDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdMember);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateMember(@PathVariable Long id, 
                                        @Valid @RequestBody MemberDTO memberDTO) {
        try {
            return memberService.updateMember(id, memberDTO)
                    .map(member -> ResponseEntity.ok(member))
                    .orElse(ResponseEntity.notFound().build());
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMember(@PathVariable Long id) {
        if (memberService.deleteMember(id)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    @PutMapping("/{id}/{status}")
    public ResponseEntity<MemberDTO> updateMembershipStatus(
            @PathVariable Long id, @PathVariable Member.MembershipStatus status) {
        try {
            // Member.MembershipStatus status = Member.MembershipStatus.valueOf(request.get("status"));
            return memberService.updateMembershipStatus(id, status)
                    .map(member -> ResponseEntity.ok(member))
                    .orElse(ResponseEntity.notFound().build());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }
}
