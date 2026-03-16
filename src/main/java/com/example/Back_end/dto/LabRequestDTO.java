package com.example.Back_end.dto;

import lombok.Data;

@Data
public class LabRequestDTO {
    private String labName;
    private String labCode;
    private String location;
    private String description;
}
