package com.example.Back_end.dto;

import lombok.Data;

@Data
public class RoomResponseDTO {
    private Long roomId;
    private String roomName;
    private String roomCode;
    private Integer capacity;
    private String status;
    private String labName;   // chỉ hiển thị tên Lab, không show ID
}
