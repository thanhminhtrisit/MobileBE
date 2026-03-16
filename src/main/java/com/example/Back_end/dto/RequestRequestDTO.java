package com.example.Back_end.dto;

import lombok.Data;

import java.util.List;

@Data
public class RequestRequestDTO {
    private String requestType;
    private Long memberId;
    private Long labId;
    private Long roomId; // optional, chỉ dùng khi approved
    private String title;
    private String description;
    private String status; // Enum dưới dạng String
    private List<Long> roomSlotIds; // chỉ bắt buộc với loại booking
}
