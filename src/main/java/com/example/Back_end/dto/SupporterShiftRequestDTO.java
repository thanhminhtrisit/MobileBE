package com.example.Back_end.dto;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Data
public class SupporterShiftRequestDTO {
    private LocalDate shiftDate;
    private LocalTime startTime;
    private LocalTime endTime;
    private List<Long> supporterIds; // danh sách supporter tham gia ca
}
