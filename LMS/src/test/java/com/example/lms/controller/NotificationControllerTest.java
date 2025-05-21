package com.example.lms.controller;

import com.example.lms.entity.Notification;
import com.example.lms.service.NotificationService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(classes = com.example.lms.LmsApplication.class)
@AutoConfigureMockMvc
class NotificationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private NotificationService notificationService;

    @Test
    void testGetAllNotifications() throws Exception {
        when(notificationService.getAllNotifications()).thenReturn(List.of(new Notification()));

        mockMvc.perform(get("/api/notifications"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    void testSendNotification() throws Exception {
        Notification notification = new Notification();
        when(notificationService.sendNotification(any(Notification.class))).thenReturn(notification);

        String notificationJson = "{\"message\":\"Test Notification\",\"memberId\":1}";

        mockMvc.perform(post("/api/notifications")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(notificationJson))
                .andExpect(status().isOk());
    }
}