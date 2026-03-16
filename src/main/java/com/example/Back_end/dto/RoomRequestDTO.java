package com.example.Back_end.dto;

import lombok.Data;

@Data
public class RoomRequestDTO {
    private Long labId;       // FK đến Lab
    private String roomName;
    private String roomCode;
    private Integer capacity;
    private String status;
}
