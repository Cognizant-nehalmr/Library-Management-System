package com.example.lms.service;

import com.example.lms.entity.Notification;
import com.example.lms.repository.NotificationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class NotificationServiceTest {

    @Mock
    private NotificationRepository notificationRepository;

    @InjectMocks
    private NotificationService notificationService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllNotifications() {
        List<Notification> notifications = List.of(new Notification());
        when(notificationRepository.findAll()).thenReturn(notifications);

        List<Notification> result = notificationService.getAllNotifications();

        assertEquals(1, result.size());
        verify(notificationRepository, times(1)).findAll();
    }

    @Test
    void testSendNotification() {
        Notification notification = new Notification();
        when(notificationRepository.save(notification)).thenReturn(notification);

        Notification result = notificationService.sendNotification(notification);

        assertNotNull(result);
        verify(notificationRepository, times(1)).save(notification);
    }
}