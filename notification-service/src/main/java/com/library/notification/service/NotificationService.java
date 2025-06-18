package com.library.notification.service;

import com.library.notification.client.MemberServiceClient;
import com.library.notification.client.TransactionServiceClient;
import com.library.notification.dto.NotificationDTO;
import com.library.notification.entity.Notification;
import com.library.notification.repository.NotificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class NotificationService {

    @Autowired
    private NotificationRepository notificationRepository;

    @Autowired
    private EmailService emailService;

    @Autowired
    private MemberServiceClient memberServiceClient;

    @Autowired
    private TransactionServiceClient transactionServiceClient;

    public List<NotificationDTO> getAllNotifications() {
        return notificationRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public Optional<NotificationDTO> getNotificationById(Long id) {
        return notificationRepository.findById(id)
                .map(this::convertToDTO);
    }

    public List<NotificationDTO> getNotificationsByMemberId(Long memberId) {
        return notificationRepository.findByMemberId(memberId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<NotificationDTO> getNotificationsByStatus(Notification.NotificationStatus status) {
        return notificationRepository.findByStatus(status).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public NotificationDTO createNotification(NotificationDTO notificationDTO) {
        Notification notification = convertToEntity(notificationDTO);
        Notification savedNotification = notificationRepository.save(notification);
        
        // Send notification asynchronously
        sendNotificationAsync(savedNotification);
        
        return convertToDTO(savedNotification);
    }

    public NotificationDTO createDueReminderNotification(Long memberId, String memberEmail, 
                                                        String bookTitle, String dueDate) {
        String subject = "Book Due Reminder - Library Management System";
        String message = String.format(
            "Dear Member,\n\nThis is a reminder that your borrowed book '%s' is due on %s. " +
            "Please return it on time to avoid any fines.\n\nThank you,\nLibrary Management System",
            bookTitle, dueDate
        );

        NotificationDTO notification = new NotificationDTO(
            memberId, message, Notification.NotificationType.DUE_REMINDER, memberEmail, subject
        );

        return createNotification(notification);
    }

    public NotificationDTO createOverdueNotification(Long memberId, String memberEmail, 
                                                   String bookTitle, int overdueDays) {
        String subject = "Overdue Book Alert - Library Management System";
        String message = String.format(
            "Dear Member,\n\nYour borrowed book '%s' is overdue by %d day(s). " +
            "Please return it immediately to avoid additional fines.\n\nThank you,\nLibrary Management System",
            bookTitle, overdueDays
        );

        NotificationDTO notification = new NotificationDTO(
            memberId, message, Notification.NotificationType.OVERDUE_ALERT, memberEmail, subject
        );

        return createNotification(notification);
    }

    public NotificationDTO createFineNotification(Long memberId, String memberEmail, 
                                                String bookTitle, String fineAmount) {
        String subject = "Fine Notice - Library Management System";
        String message = String.format(
            "Dear Member,\n\nA fine of $%s has been applied to your account for the overdue book '%s'. " +
            "Please pay the fine at your earliest convenience.\n\nThank you,\nLibrary Management System",
            fineAmount, bookTitle
        );

        NotificationDTO notification = new NotificationDTO(
            memberId, message, Notification.NotificationType.FINE_NOTICE, memberEmail, subject
        );

        return createNotification(notification);
    }

    @Async
    public void sendNotificationAsync(Notification notification) {
        try {
            if (notification.getRecipientEmail() != null && !notification.getRecipientEmail().isEmpty()) {
                emailService.sendSimpleEmail(
                    notification.getRecipientEmail(),
                    notification.getSubject(),
                    notification.getMessage()
                );
                
                notification.setStatus(Notification.NotificationStatus.SENT);
                notification.setDateSent(LocalDateTime.now());
            } else {
                notification.setStatus(Notification.NotificationStatus.FAILED);
                notification.setErrorMessage("No recipient email provided");
            }
        } catch (Exception e) {
            notification.setStatus(Notification.NotificationStatus.FAILED);
            notification.setErrorMessage(e.getMessage());
            notification.setRetryCount(notification.getRetryCount() + 1);
        }
        
        notificationRepository.save(notification);
    }

    @Scheduled(fixedRate = 300000) // Run every 5 minutes
    public void processPendingNotifications() {
        List<Notification> pendingNotifications = notificationRepository.findPendingNotifications();
        
        for (Notification notification : pendingNotifications) {
            if (notification.getRetryCount() < 3) {
                sendNotificationAsync(notification);
            }
        }
    }

    @Scheduled(cron = "0 0 9 * * ?") // Run daily at 9 AM
    public void sendDueReminders() {
        try {
            // This would integrate with transaction service to get due books
            // For now, we'll create a placeholder implementation
            System.out.println("Processing due reminders at: " + LocalDateTime.now());
        } catch (Exception e) {
            System.err.println("Error processing due reminders: " + e.getMessage());
        }
    }

    public Map<String, Object> getNotificationStats() {
        Map<String, Object> stats = new HashMap<>();
        LocalDateTime lastWeek = LocalDateTime.now().minusWeeks(1);
        
        stats.put("totalNotifications", notificationRepository.count());
        stats.put("sentLastWeek", notificationRepository.countSentNotificationsSince(lastWeek));
        stats.put("pendingNotifications", notificationRepository.findByStatus(Notification.NotificationStatus.PENDING).size());
        stats.put("failedNotifications", notificationRepository.findByStatus(Notification.NotificationStatus.FAILED).size());
        
        return stats;
    }

    private NotificationDTO convertToDTO(Notification notification) {
        NotificationDTO dto = new NotificationDTO();
        dto.setNotificationId(notification.getNotificationId());
        dto.setMemberId(notification.getMemberId());
        dto.setMessage(notification.getMessage());
        dto.setType(notification.getType());
        dto.setStatus(notification.getStatus());
        dto.setDateSent(notification.getDateSent());
        dto.setRecipientEmail(notification.getRecipientEmail());
        dto.setSubject(notification.getSubject());
        dto.setRetryCount(notification.getRetryCount());
        dto.setErrorMessage(notification.getErrorMessage());
        return dto;
    }

    private Notification convertToEntity(NotificationDTO dto) {
        Notification notification = new Notification();
        notification.setMemberId(dto.getMemberId());
        notification.setMessage(dto.getMessage());
        notification.setType(dto.getType());
        notification.setRecipientEmail(dto.getRecipientEmail());
        notification.setSubject(dto.getSubject());
        if (dto.getStatus() != null) {
            notification.setStatus(dto.getStatus());
        }
        return notification;
    }
}
