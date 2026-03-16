package com.example.Back_end.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class IncidentResponseDTO {
    private Long incidentId;

    private String incidentTypeName; // Không show ID, chỉ show tên
    private String supporterName;
    private String labName;

    private String title;
    private String description;
    private String severity;
    private String status;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime reportedAt;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime resolvedAt;
}
