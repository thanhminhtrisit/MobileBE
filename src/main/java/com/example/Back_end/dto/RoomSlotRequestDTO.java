package com.example.Back_end.dto;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
public class RoomSlotRequestDTO {
    private String slotName;
    private LocalTime startTime;
    private LocalTime endTime;
    private LocalDate bookingDate;
    private String status;
    private Boolean isAvailable;
}
