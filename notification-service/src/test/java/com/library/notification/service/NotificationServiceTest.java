package com.library.notification.service;

import com.library.notification.dto.NotificationDTO;
import com.library.notification.entity.Notification;
import com.library.notification.repository.NotificationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import java.util.Optional;
import java.util.Collections;
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
    void testGetNotificationById_Success() {
        Notification notification = new Notification();
        notification.setNotificationId(1L);
        notification.setMessage("Test message");
        when(notificationRepository.findById(1L)).thenReturn(Optional.of(notification));
        Optional<NotificationDTO> result = notificationService.getNotificationById(1L);
        assertTrue(result.isPresent());
        assertEquals(1L, result.get().getNotificationId());
        assertEquals("Test message", result.get().getMessage());
    }

    @Test
    void testGetNotificationById_NotFound() {
        when(notificationRepository.findById(2L)).thenReturn(Optional.empty());
        Optional<NotificationDTO> result = notificationService.getNotificationById(2L);
        assertFalse(result.isPresent());
    }

    @Test
    void testGetAllNotifications_Empty() {
        when(notificationRepository.findAll()).thenReturn(Collections.emptyList());
        List<NotificationDTO> result = notificationService.getAllNotifications();
        assertTrue(result.isEmpty());
    }

    // Add more tests for notification creation, reminders, and edge cases
}
