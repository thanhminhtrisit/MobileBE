package com.example.Back_end.dto;

public record RegisterRequest(
        String username,
        String email,
        String fullName,
        String phone
) {}
