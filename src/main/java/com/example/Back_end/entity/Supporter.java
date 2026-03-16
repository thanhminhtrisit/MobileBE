package com.example.Back_end.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "supporters")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Supporter {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long supporterId;

    @OneToOne
    @JoinColumn(name = "user_id", unique = true)
    private User user;

    private String supporterCode;
    private String status;
    private LocalDateTime registeredAt;

    // ✅ Một supporter có thể trực nhiều ca
    @ManyToMany(mappedBy = "supporters")
    private List<SupporterShift> supporterShifts;
}
