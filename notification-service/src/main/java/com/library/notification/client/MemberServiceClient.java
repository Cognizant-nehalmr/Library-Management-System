package com.library.notification.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Map;

@FeignClient(name = "member-service")
public interface MemberServiceClient {
    
    @GetMapping("/api/members/{id}")
    ResponseEntity<Map<String, Object>> getMemberById(@PathVariable("id") Long memberId);
}
