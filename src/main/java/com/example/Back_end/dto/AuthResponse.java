package com.example.Back_end.dto;

import com.example.Back_end.enums.Role;
import lombok.Data;

@Data
public class AuthResponse {
    private String token;
    private Long userId;
    private String fullName;
    private String email;
    private Role role;
}
