package com.example.Back_end.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class DashboardUser {
    private Long userId;
    private String username;
    private String email;
    private String displayName;     // = fullName
    private String phone;
    private String uid;             // = firebaseUid
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String photoURL;
    private Boolean emailVerified;
    private String providerId;
}
