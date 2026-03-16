package com.example.Back_end.dto;

import lombok.Data;

@Data
public class IncidentTypeRequestDTO {
    private String typeName;
    private String description;
    private String priorityLevel;
}
