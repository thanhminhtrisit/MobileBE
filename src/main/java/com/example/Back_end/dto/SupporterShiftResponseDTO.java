package com.example.Back_end.dto;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Data
public class SupporterShiftResponseDTO {
    private Long supporterShiftId;
    private LocalDate shiftDate;
    private LocalTime startTime;
    private LocalTime endTime;
    private List<String> supporterCodes; // danh sách mã supporter trong ca
}
