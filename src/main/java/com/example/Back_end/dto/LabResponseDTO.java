package com.example.Back_end.dto;

import lombok.Data;

@Data
public class LabResponseDTO {
    private Long labId;
    private String labName;
    private String labCode;
    private String location;
    private String description;
    private String status;
}
