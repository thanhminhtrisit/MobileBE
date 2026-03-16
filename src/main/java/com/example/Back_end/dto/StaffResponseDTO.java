package com.example.Back_end.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class StaffResponseDTO {
    private Long staffId;
    private Long userId;
    private List<Long> labIds;
    private String staffCode;
    private String position;
    private String department;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime hiredAt;
}
