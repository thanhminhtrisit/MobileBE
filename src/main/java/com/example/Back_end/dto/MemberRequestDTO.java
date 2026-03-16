package com.example.Back_end.dto;

import lombok.Data;

@Data
public class MemberRequestDTO {
    private Long userId;
    private Long labId;
    private String memberCode;
    private String role;
}
