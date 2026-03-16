package com.example.Back_end.dto;

import lombok.Data;

@Data
public class StaffRequestDTO {
    private Long userId;
    private Long labId;
    private String staffCode;
    private String position;
    private String department;
}
