package com.example.Back_end.dto;

import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class LabAssignRoomSlotByDateDTO {
    private Long labId;
    private LocalDate bookingDate;
    private List<String> slotNames;
}
