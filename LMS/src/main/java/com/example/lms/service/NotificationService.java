package com.example.lms.service;

import com.example.lms.entity.Notification;
import com.example.lms.repository.NotificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class NotificationService {

    @Autowired
    private NotificationRepository notificationRepository;

    public List<Notification> getAllNotifications() {
        return notificationRepository.findAll();
    }

    public Optional<Notification> getNotificationById(Long id) {
        return notificationRepository.findById(id);
    }

    public Notification sendNotification(Notification notification) {
        notification.setDateSent(LocalDate.now());
        return notificationRepository.save(notification);
    }

    public Optional<Notification> updateNotification(Long id, Notification notification) {
        return notificationRepository.findById(id).map(existingNotification -> {
            existingNotification.setMessage(notification.getMessage());
            existingNotification.setDateSent(notification.getDateSent());
            return notificationRepository.save(existingNotification);
        });
    }

    public List<Notification> getNotificationsByMember(Long memberId) {
        return notificationRepository.findAll().stream()
                .filter(notification -> notification.getMember().getMemberId().equals(memberId))
                .collect(Collectors.toList());
    }
}