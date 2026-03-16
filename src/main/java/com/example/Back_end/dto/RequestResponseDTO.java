package com.example.Back_end.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class RequestResponseDTO {
    private Long requestId;
    private String requestType;
    private String memberName;
    private String staffName;
    private String supporterName;
    private String labName;
    private String roomName;
    private String title;
    private String description;
    private String status;
    private List<String> roomSlots; // hiển thị danh sách slot đã đặt

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime requestedAt;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime approvedAt;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime completedAt;
}
