package com.library.transaction.client;



import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.library.transaction.dto.MemberDTO;

@FeignClient(name = "member-service")
public interface MemberServiceClient {
    @GetMapping("/api/members/{id}")
    ResponseEntity<MemberDTO> getMemberById(@PathVariable("id") Long memberId);
}
