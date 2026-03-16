package com.example.Back_end.dto;

import lombok.Data;

@Data
public class RoomEquipmentResponseDTO {
    private Long equipmentId;
    private String equipmentName;
    private String equipmentCode;
    private String category;
    private String status;
    private String roomName;
}
