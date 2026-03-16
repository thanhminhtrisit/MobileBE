package com.example.Back_end.dto;

import lombok.Data;

@Data
public class IncidentTypeResponseDTO {
    private Long incidentTypeId;
    private String typeName;
    private String description;
    private String priorityLevel;
}
