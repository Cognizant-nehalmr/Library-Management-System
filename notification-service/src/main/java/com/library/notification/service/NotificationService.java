package com.library.notification.service;

import com.library.notification.client.FineServiceClient;
import com.library.notification.client.MemberServiceClient;
import com.library.notification.client.TransactionServiceClient;
import com.library.notification.dto.FineResponseDTO;
import com.library.notification.dto.MemberDTO;
import com.library.notification.dto.BorrowingTransactionResponseDTO;
import com.library.notification.dto.FineDTO;
import com.library.notification.dto.NotificationDTO;
import com.library.notification.entity.Notification;
import com.library.notification.entity.Notification.NotificationStatus;
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
import java.time.LocalDate;
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

    @Autowired
    private TransactionServiceClient transactionServiceClient;

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

    @Async
    public void sendCustomEmail(Long memberId, String memberName, String memberEmail, String subject, String customMessage) {
        Map<String, Object> variables = new HashMap<>();
        variables.put("memberName", memberName); 
        variables.put("subject", subject);
        variables.put("customMessage", customMessage);
        

        try {
            // Send email
            emailService.sendHtmlEmail(memberEmail, subject, "custom-email-template", variables);

            // Save success notification
            Notification notification = convertToEntity(new NotificationDTO(
                    memberId,
                    "Custom email sent successfully.",
                    Notification.NotificationType.CUSTOM,
                    memberEmail,
                    subject));
            notification.setStatus(Notification.NotificationStatus.SENT);
            notification.setDateSent(LocalDateTime.now());
            notificationRepository.save(notification);

        } catch (Exception e) {
            // Save failed notification
            Notification failedNotification = convertToEntity(new NotificationDTO(
                    memberId,
                    "Failed to send custom email: " + e.getMessage(),
                    Notification.NotificationType.CUSTOM,
                    memberEmail,
                    subject));
            failedNotification.setStatus(Notification.NotificationStatus.FAILED);
            failedNotification.setDateSent(LocalDateTime.now());
            notificationRepository.save(failedNotification);
        }
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

    @Async
    public void processUpcomingDueAlerts() {
        List<BorrowingTransactionResponseDTO> transactions = transactionServiceClient.getAllTransactions();

        LocalDate today = LocalDate.now();
        LocalDate upcomingLimit = today.plusDays(3);

        transactions.stream()
                .filter(transaction -> transaction.getDueDate() != null &&
                        !transaction.getDueDate().isBefore(today) && // dueDate >= today
                        !transaction.getDueDate().isAfter(upcomingLimit) && // dueDate <= today + 3
                        transaction.getStatus() != null && transaction.getStatus().equals("BORROWED") &&
                        transaction.getMember() != null &&
                        transaction.getBook() != null)
                .forEach(transaction -> {
                    try {
                        Long memberId = transaction.getMember().getMemberId();
                        String memberEmail = transaction.getMember().getEmail();
                        String memberName = transaction.getMember().getName();
                        String bookTitle = transaction.getBook().getTitle();
                        String dueDate = transaction.getDueDate().toString();

                        // Send upcoming due date alert
                        System.out.println(transaction.getMember().getName()
                                + "                                                                                    ");
                        createOverdueAlert(memberId, memberEmail, memberName, bookTitle, dueDate);

                    } catch (Exception e) {
                        log.error("Failed to send due soon alert for transactionId: {}, error: {}",
                                transaction.getTransactionId(), e.getMessage(), e);
                    }
                });
    }

    @Async
    public void createOverdueAlert(Long memberId, String memberEmail, String memberName, String bookTitle,
            String dueDate) {
        String subject = "Overdue Alert - Library Management System";

        Map<String, Object> variables = new HashMap<>();
        variables.put("memberName", memberName);
        variables.put("bookTitle", bookTitle);
        variables.put("dueDate", dueDate);

        try {
            emailService.sendHtmlEmail(memberEmail, subject, "due-reminder", variables);
            // NotificationDTO notificationDTO = ;
            Notification successfullNotification = convertToEntity(new NotificationDTO(
                    memberId,
                    "An overdue alert email has been sent.",
                    Notification.NotificationType.OVERDUE_ALERT,
                    memberEmail,
                    subject));
            successfullNotification.setStatus(NotificationStatus.SENT);
            successfullNotification.setDateSent(LocalDateTime.now());
            notificationRepository.save(successfullNotification);

            // save notification (optional)
            // createNotification(notification);

        } catch (Exception e) {
            // NotificationDTO failedNotification =
            Notification failedNotification = convertToEntity(new NotificationDTO(
                    memberId,
                    "Failed to send overdue alert email: " + e.getMessage(),
                    Notification.NotificationType.OVERDUE_ALERT,
                    memberEmail,
                    subject));
            failedNotification.setStatus(NotificationStatus.FAILED);
            failedNotification.setDateSent(LocalDateTime.now());
            notificationRepository.save(failedNotification);
            // createNotification(failedNotification);
        }
    }

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
            Notification successfulNotification = convertToEntity(new NotificationDTO(
                    memberId,
                    "An Fine alert email has been sent.",
                    Notification.NotificationType.FINE_NOTICE,
                    memberEmail,
                    subject));
            successfulNotification.setStatus(Notification.NotificationStatus.SENT);
            successfulNotification.setDateSent(LocalDateTime.now());
            notificationRepository.save(successfulNotification);
        } catch (Exception e) {
            // Create failure notification with error message
            Notification failedNotification = convertToEntity(new NotificationDTO(
                    memberId,
                    "An Fine alert email failed to send..",
                    Notification.NotificationType.FINE_NOTICE,
                    memberEmail,
                    subject));
            failedNotification.setStatus(Notification.NotificationStatus.FAILED);
            failedNotification.setDateSent(LocalDateTime.now());
            notificationRepository.save(failedNotification);
            // createNotification(notification);

            log.error("Error sending fine email to {}: {}", memberEmail, e.getMessage(), e);
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
