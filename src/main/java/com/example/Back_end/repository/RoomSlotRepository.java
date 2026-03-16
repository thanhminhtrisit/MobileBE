package com.example.Back_end.repository;

import com.example.Back_end.entity.RoomSlot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface RoomSlotRepository extends JpaRepository<RoomSlot, Long> {
    List<RoomSlot> findByBookingDate(LocalDate bookingDate);

    List<RoomSlot> findByIsAvailableTrue();

    List<RoomSlot> findBySlotName(String slotName);

    @Query("SELECT rs FROM RoomSlot rs WHERE rs.startTime = :startTime AND rs.endTime = :endTime")
    List<RoomSlot> findByTimeRange(@Param("startTime") java.time.LocalTime startTime,
                                   @Param("endTime") java.time.LocalTime endTime);

    boolean existsByBookingDateBetween(LocalDate startDate, LocalDate endDate);
}
