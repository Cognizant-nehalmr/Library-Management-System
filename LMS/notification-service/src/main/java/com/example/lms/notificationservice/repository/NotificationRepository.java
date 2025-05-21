package com.example.lms.notificationservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.lms.notificationservice.entity.Notification;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
    // Additional query methods can be added here
}