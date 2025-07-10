package com.library.notification.service;

import com.library.notification.client.FineServiceClient;
import com.library.notification.client.MemberServiceClient;
import com.library.notification.client.TransactionServiceClient;
import com.library.notification.dto.FineResponseDTO;
import com.library.notification.dto.FineDTO;
import com.library.notification.dto.NotificationDTO;
import com.library.notification.entity.Notification;
import com.library.notification.repository.NotificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class NotificationService {

    private static final Logger log = LoggerFactory.getLogger(NotificationService.class);

    @Autowired
    private NotificationRepository notificationRepository;

    @Autowired
    private EmailService emailService;

    // @Autowired
    // private MemberServiceClient memberServiceClient;

    // @Autowired
    // private TransactionServiceClient transactionServiceClient;

    @Autowired
    private FineServiceClient fineServiceClient;

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

    @Async
    public void processPendingFineNotifications() {
        List<FineResponseDTO> fines = fineServiceClient.getAllFines();

        fines.stream()
                .filter(fine -> fine.getFineDTO() != null
                        && fine.getFineDTO().getStatus() == FineDTO.FineStatus.PENDING
                        && fine.getBorrowingTransactionResponseDTO() != null
                        && fine.getBorrowingTransactionResponseDTO().getMember() != null
                        && fine.getBorrowingTransactionResponseDTO().getBook() != null)
                .forEach(fine -> {
                    try {
                        Long memberId = fine.getBorrowingTransactionResponseDTO().getMember().getMemberId();
                        String memberEmail = fine.getBorrowingTransactionResponseDTO().getMember().getEmail();
                        String memberName = fine.getBorrowingTransactionResponseDTO().getMember().getName();
                        String bookTitle = fine.getBorrowingTransactionResponseDTO().getBook().getTitle();
                        String fineType = fine.getFineDTO().getFineType().name(); // Get fine type
                        // Safely format fine amount
                        String fineAmount = fine.getFineDTO().getAmount()
                                .setScale(2, RoundingMode.HALF_UP)
                                .toString();

                        // Send notification
                        createFineNotification(memberId, memberEmail, memberName, bookTitle, fineAmount, fineType);

                    } catch (Exception e) {

                        // Optional: Log specific fine ID or transaction ID for traceability
                        log.error("Failed to send fine notification for fineId: {}, error: {}",
                                fine.getFineDTO().getFineId(), e.getMessage(), e);
                    }
                });
    }

    // public NotificationDTO createDueReminderNotification(Long memberId, String
    // memberEmail,
    // String bookTitle, String dueDate) {
    // String subject = "Book Due Reminder - Library Management System";
    // String message = String.format(
    // "Dear Member,\n\nThis is a reminder that your borrowed book '%s' is due on
    // %s. " +
    // "Please return it on time to avoid any fines.\n\nThank you,\nLibrary
    // Management System",
    // bookTitle, dueDate);

    // NotificationDTO notification = new NotificationDTO(
    // memberId, message, Notification.NotificationType.DUE_REMINDER, memberEmail,
    // subject);

    // return createNotification(notification);
    // }

    // @Async
    // public void createOverdueNotification(Long memberId, String memberEmail,
    // String memberName, String bookTitle,
    // String dueDate, int overdueDays) {
    // String subject = "Overdue Book Alert - Library Management System";

    // // 1. Prepare Thymeleaf variables
    // Map<String, Object> variables = new HashMap<>();
    // variables.put("memberName", memberName);
    // variables.put("bookTitle", bookTitle);
    // variables.put("dueDate", dueDate); // Format: "2025-07-15"

    // try {
    // // 2. Send HTML email
    // emailService.sendHtmlEmail(
    // memberEmail,
    // subject,
    // "due-reminder", // Thymeleaf template name without `.html`
    // variables);

    // // 3. Create notification object with status SENT
    // NotificationDTO notification = new NotificationDTO(
    // memberId,
    // "HTML Email Sent: Overdue Book Alert", // Message for record/log
    // Notification.NotificationType.OVERDUE_ALERT,
    // memberEmail,
    // subject);
    // notification.setStatus(Notification.NotificationStatus.SENT);
    // notification.setDateSent(LocalDateTime.now());

    // // return createNotification(notification);

    // } catch (Exception e) {
    // // 4. On failure
    // NotificationDTO notification = new NotificationDTO(
    // memberId,
    // "Failed to send overdue alert email: " + e.getMessage(),
    // Notification.NotificationType.OVERDUE_ALERT,
    // memberEmail,
    // subject);
    // notification.setStatus(Notification.NotificationStatus.FAILED);
    // notification.setErrorMessage(e.getMessage());

    // // return createNotification(notification);
    // }
    // }

    @Async
    public void createFineNotification(Long memberId, String memberEmail, String memberName, String bookTitle,
            String fineAmount, String fineType) {
        String subject = "Fine Notice - Library Management System";

        // HTML template variables
        Map<String, Object> variables = new HashMap<>();
        variables.put("memberName", memberName);
        variables.put("bookTitle", bookTitle);
        variables.put("fineAmount", fineAmount);
        variables.put("fineType", fineType); // Add fine type to the template variables

        try {
            // Send HTML email using Thymeleaf template
            emailService.sendHtmlEmail(memberEmail, subject, "fine-notification-template", variables);
        } catch (Exception e) {
            // Create failure notification with error message
            NotificationDTO notification = new NotificationDTO(
                    memberId,
                    "Failed to send fine notification email: " + e.getMessage(),
                    Notification.NotificationType.FINE_NOTICE,
                    memberEmail,
                    subject);
            createNotification(notification);
            log.error("Error sending fine email to {}: {}", memberEmail, e.getMessage(), e);
        }
    }

    @Async
    public void sendNotificationAsync(Notification notification) {
        try {
            if (notification.getRecipientEmail() != null && !notification.getRecipientEmail().isEmpty()) {
                emailService.sendSimpleEmail(
                        notification.getRecipientEmail(),
                        notification.getSubject(),
                        notification.getMessage());

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
        stats.put("pendingNotifications",
                notificationRepository.findByStatus(Notification.NotificationStatus.PENDING).size());
        stats.put("failedNotifications",
                notificationRepository.findByStatus(Notification.NotificationStatus.FAILED).size());

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
