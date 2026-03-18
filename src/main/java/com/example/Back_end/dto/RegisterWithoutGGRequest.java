package com.example.Back_end.dto;

import lombok.Data;

@Data
public class RegisterWithoutGGRequest {
    private String username;
    private String email;
    private String password;
    private String fullName;
    private String phone;
}
