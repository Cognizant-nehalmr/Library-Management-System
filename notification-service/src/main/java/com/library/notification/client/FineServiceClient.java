package com.library.notification.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import com.library.notification.dto.FineResponseDTO;

import java.util.List;

@FeignClient(name = "fine-service")
public interface FineServiceClient {
    @GetMapping("/api/fines")
    List<FineResponseDTO> getAllFines();
}
