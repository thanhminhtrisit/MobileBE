package com.example.Back_end.entity;

import com.example.Back_end.enums.LabStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name = "labs")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Lab {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long labId;

    private String labName;
    private String labCode;
    private String location;
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private LabStatus status = LabStatus.ACTIVE;

    // Một Lab có nhiều Room
    @OneToMany(mappedBy = "lab", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Room> rooms;

    @ManyToMany
    @JoinTable(
            name = "lab_room_slot",
            joinColumns = @JoinColumn(name = "lab_id"),
            inverseJoinColumns = @JoinColumn(name = "room_slot_id")
    )
    private List<RoomSlot> roomSlots;


    @ManyToMany(mappedBy = "labs")
    private List<Member> members;

    // ✅ Many-to-Many với Staff
    @ManyToMany(mappedBy = "labs")
    private List<Staff> staffs;
}
