package com.example.Back_end.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class RoomEquipmentRequestDTO {
    private Long roomId;
    private String equipmentName;
    private String equipmentCode;
    private String category;
    private String status;
}
