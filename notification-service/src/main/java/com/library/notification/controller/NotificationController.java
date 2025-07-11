package com.library.notification.controller;

import com.library.notification.dto.NotificationDTO;
import com.library.notification.entity.Notification;
import com.library.notification.service.NotificationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/notifications")
@CrossOrigin(origins = "*")
@Tag(name = "Notification Management", description = "APIs for managing notifications and alerts")
public class NotificationController {

    @Autowired
    private NotificationService notificationService;

    @GetMapping
    @Operation(summary = "Get all notifications", description = "Retrieve a list of all notifications")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved notifications"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<List<NotificationDTO>> getAllNotifications() {
        List<NotificationDTO> notifications = notificationService.getAllNotifications();
        return ResponseEntity.ok(notifications);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get notification by ID", description = "Retrieve a specific notification by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved notification"),
            @ApiResponse(responseCode = "404", description = "Notification not found")
    })
    public ResponseEntity<NotificationDTO> getNotificationById(
            @Parameter(description = "Notification ID") @PathVariable Long id) {
        return notificationService.getNotificationById(id)
                .map(notification -> ResponseEntity.ok(notification))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/member/{memberId}")
    @Operation(summary = "Get notifications by member ID", description = "Retrieve all notifications for a specific member")
    public ResponseEntity<List<NotificationDTO>> getNotificationsByMemberId(
            @Parameter(description = "Member ID") @PathVariable Long memberId) {
        List<NotificationDTO> notifications = notificationService.getNotificationsByMemberId(memberId);
        return ResponseEntity.ok(notifications);
    }

    @GetMapping("/status/{status}")
    @Operation(summary = "Get notifications by status", description = "Retrieve notifications filtered by status")
    public ResponseEntity<List<NotificationDTO>> getNotificationsByStatus(
            @Parameter(description = "Notification status") @PathVariable Notification.NotificationStatus status) {
        List<NotificationDTO> notifications = notificationService.getNotificationsByStatus(status);
        return ResponseEntity.ok(notifications);
    }

    @PostMapping("/custom")
    public ResponseEntity<String> sendCustomEmail(
            @RequestParam Long memberId,
            @RequestParam String memberName,
            @RequestParam String memberEmail,
            @RequestParam String subject,
            @RequestParam String message) {
        try {
            notificationService.sendCustomEmail(memberId, memberName, memberEmail, subject, message);
            return ResponseEntity.ok("Custom email is being sent.");
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body("Failed to send custom email: " + e.getMessage());
        }
    }

    @PostMapping("/fines/process")
    @Operation(summary = "Trigger fine notifications processing", description = "Process pending fine notifications")
    public ResponseEntity<String> triggerFineNotifications() {
        notificationService.processPendingFineNotifications();
        return ResponseEntity.ok("Fine notifications processing triggered.");
    }

    @PostMapping("/overdue/process")
    @Operation(summary = "Trigger overdue notifications processing", description = "Process pending overdue notifications")
    public ResponseEntity<String> triggerOverdueNotifications() {
        notificationService.processUpcomingDueAlerts();
        return ResponseEntity.ok("Overdue notifications processing triggered.");
    }

    
    @GetMapping("/stats")
    @Operation(summary = "Get notification statistics", description = "Retrieve notification statistics and metrics")
    public ResponseEntity<Map<String, Object>> getNotificationStats() {
        Map<String, Object> stats = notificationService.getNotificationStats();
        return ResponseEntity.ok(stats);
    }
}
