package com.example.Back_end.dto;

import lombok.Data;

@Data
public class SupporterRequestDTO {
    private Long userId;
    private String supporterCode;
    private String status;
}
