package com.example.Back_end.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "incident_types")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class IncidentType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long incidentTypeId;
    private String typeName;
    private String description;
    private String priorityLevel;
}
