package com.example.Back_end.dto;

import lombok.Data;

@Data
public class UserRequestDT0 {
    private String username;
    private String email;
    private String fullName;
    private String phone;
    private Boolean admin;
}
