package com.example.lms.notificationservice.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.lms.notificationservice.repository.NotificationRepository;
import com.example.lms.notificationservice.entity.Notification;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class NotificationService {

    private static final Logger logger = LoggerFactory.getLogger(NotificationService.class);

    @Autowired
    private NotificationRepository notificationRepository;

    public List<Notification> getAllNotifications() {
        logger.info("Fetching all notifications");
        return notificationRepository.findAll();
    }

    public Notification sendNotification(Notification notification) {
        logger.info("Sending notification to recipient: {}", notification.getRecipient());
        return notificationRepository.save(notification);
    }

    // Additional service methods can be added here
}