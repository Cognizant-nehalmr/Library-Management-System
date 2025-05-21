package com.example.lms.notificationservice.service;

import com.example.lms.notificationservice.entity.Notification;
import com.example.lms.notificationservice.repository.NotificationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class NotificationServiceTest {

    @InjectMocks
    private NotificationService notificationService;

    @Mock
    private NotificationRepository notificationRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllNotifications() {
        // Arrange
        Notification notification1 = new Notification();
        notification1.setId(1L);
        notification1.setMessage("Message 1");

        Notification notification2 = new Notification();
        notification2.setId(2L);
        notification2.setMessage("Message 2");

        when(notificationRepository.findAll()).thenReturn(Arrays.asList(notification1, notification2));

        // Act
        List<Notification> notifications = notificationService.getAllNotifications();

        // Assert
        assertEquals(2, notifications.size());
        verify(notificationRepository, times(1)).findAll();
    }

    @Test
    void testSendNotification() {
        // Arrange
        Notification notification = new Notification();
        notification.setId(1L);
        notification.setMessage("New Message");

        when(notificationRepository.save(notification)).thenReturn(notification);

        // Act
        Notification savedNotification = notificationService.sendNotification(notification);

        // Assert
        assertEquals("New Message", savedNotification.getMessage());
        verify(notificationRepository, times(1)).save(notification);
    }
}