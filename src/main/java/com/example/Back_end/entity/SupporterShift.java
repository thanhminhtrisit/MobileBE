package com.example.Back_end.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Entity
@Table(name = "supporter_shifts")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SupporterShift {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long supporterShiftId;

    // ✅ Ngày trực
    private LocalDate shiftDate;

    // ✅ Thời gian bắt đầu & kết thúc
    private LocalTime startTime;
    private LocalTime endTime;

    // ✅ Một ca có thể có nhiều supporter
    @ManyToMany
    @JoinTable(
            name = "supporter_shift_assignments",
            joinColumns = @JoinColumn(name = "supporter_shift_id"),
            inverseJoinColumns = @JoinColumn(name = "supporter_id")
    )
    private List<Supporter> supporters;
}
