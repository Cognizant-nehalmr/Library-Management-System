package com.library.notification.controller;

import com.library.notification.dto.NotificationDTO;
import com.library.notification.service.NotificationService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import java.util.Optional;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(NotificationController.class)
class NotificationControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private NotificationService notificationService;

    @Test
    void getNotificationById_ReturnsNotification() throws Exception {
        NotificationDTO notification = new NotificationDTO();
        notification.setNotificationId(1L);
        Mockito.when(notificationService.getNotificationById(1L)).thenReturn(Optional.of(notification));
        mockMvc.perform(get("/api/notifications/1"))
                .andExpect(status().isOk());
    }

    @Test
    void sendCustomEmail_ReturnsOk() throws Exception {
        Mockito.doNothing().when(notificationService).sendCustomEmail(
                anyLong(), Mockito.anyString(), Mockito.anyString(), Mockito.anyString(), Mockito.anyString());
        mockMvc.perform(post("/api/notifications/custom")
                .param("memberId", "1")
                .param("memberName", "Test User")
                .param("memberEmail", "test@email.com")
                .param("subject", "Test Subject")
                .param("message", "Test Message"))
                .andExpect(status().isOk());
    }
}
