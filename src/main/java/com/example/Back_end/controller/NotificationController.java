package com.example.Back_end.controller;

import com.example.Back_end.dto.NotificationDTO;
import com.example.Back_end.entity.Notification;
import com.example.Back_end.service.interf.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;

    @GetMapping("/member/{memberId}")
    public ResponseEntity<List<NotificationDTO>> getMemberNotifications(@PathVariable Long memberId) {
        return ResponseEntity.ok(notificationService.getMemberNotifications(memberId));
    }

    @GetMapping("/staff/{staffId}")
    public ResponseEntity<List<NotificationDTO>> getStaffNotifications(@PathVariable Long staffId) {
        return ResponseEntity.ok(notificationService.getStaffNotifications(staffId));
    }

    @GetMapping("/supporter/{supporterId}")
    public ResponseEntity<List<NotificationDTO>> getSupporterNotifications(@PathVariable Long supporterId) {
        return ResponseEntity.ok(notificationService.getSupporterNotifications(supporterId));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<NotificationDTO>> getUserNotifications(@PathVariable Long userId) {
        return ResponseEntity.ok(notificationService.getUserNotifications(userId));
    }
}
