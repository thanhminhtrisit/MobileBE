package com.example.Back_end.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class IncidentRequestDTO {
    private Long incidentTypeId;
    private Long supporterId;
    private Long labId;

    private String title;
    private String description;
    private String severity;
    private String status;
}
