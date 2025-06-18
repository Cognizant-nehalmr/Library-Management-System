package com.library.notification.dto;

import com.library.notification.entity.Notification;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

public class NotificationDTO {
    private Long notificationId;
    
    @NotNull(message = "Member ID is required")
    private Long memberId;
    
    @NotBlank(message = "Message is required")
    private String message;
    
    @NotNull(message = "Notification type is required")
    private Notification.NotificationType type;
    
    private Notification.NotificationStatus status;
    private LocalDateTime dateSent;
    private String recipientEmail;
    private String subject;
    private Integer retryCount;
    private String errorMessage;

    // Constructors
    public NotificationDTO() {}

    public NotificationDTO(Long memberId, String message, Notification.NotificationType type, 
                          String recipientEmail, String subject) {
        this.memberId = memberId;
        this.message = message;
        this.type = type;
        this.recipientEmail = recipientEmail;
        this.subject = subject;
    }

    // Getters and Setters
    public Long getNotificationId() { return notificationId; }
    public void setNotificationId(Long notificationId) { this.notificationId = notificationId; }

    public Long getMemberId() { return memberId; }
    public void setMemberId(Long memberId) { this.memberId = memberId; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public Notification.NotificationType getType() { return type; }
    public void setType(Notification.NotificationType type) { this.type = type; }

    public Notification.NotificationStatus getStatus() { return status; }
    public void setStatus(Notification.NotificationStatus status) { this.status = status; }

    public LocalDateTime getDateSent() { return dateSent; }
    public void setDateSent(LocalDateTime dateSent) { this.dateSent = dateSent; }

    public String getRecipientEmail() { return recipientEmail; }
    public void setRecipientEmail(String recipientEmail) { this.recipientEmail = recipientEmail; }

    public String getSubject() { return subject; }
    public void setSubject(String subject) { this.subject = subject; }

    public Integer getRetryCount() { return retryCount; }
    public void setRetryCount(Integer retryCount) { this.retryCount = retryCount; }

    public String getErrorMessage() { return errorMessage; }
    public void setErrorMessage(String errorMessage) { this.errorMessage = errorMessage; }
}
