package com.example.Back_end.repository;

import com.example.Back_end.entity.RoomEquipment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoomEquipmentRepository extends JpaRepository<RoomEquipment, Long> {
}
