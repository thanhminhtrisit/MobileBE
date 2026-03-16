package com.example.Back_end.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Entity
@Table(name = "room_slots")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RoomSlot {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long roomSlotId;

    private String slotName;        // ví dụ: "Ca 1" hoặc "Morning Slot"
    private LocalTime startTime;    // ví dụ: 07:00
    private LocalTime endTime;      // ví dụ: 09:00

    private LocalDate bookingDate;
    private String status;
    private Boolean isAvailable;

    @ManyToMany(mappedBy = "roomSlots")
    private List<Request> requests;

    @ManyToMany(mappedBy = "roomSlots")
    private List<Lab> labs;
}
