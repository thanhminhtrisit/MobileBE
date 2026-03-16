package com.example.Back_end.dto;

import lombok.Data;

@Data
public class UserAssignDTO {
    private Long userId;   // chỉ cần ID user là đủ
    private Long labId;    // chỉ dùng cho Member / Staff
}
