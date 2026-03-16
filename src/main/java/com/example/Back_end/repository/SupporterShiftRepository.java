package com.example.Back_end.repository;

import com.example.Back_end.entity.Supporter;
import com.example.Back_end.entity.SupporterShift;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Repository
public interface SupporterShiftRepository extends JpaRepository<SupporterShift, Long> {

    @Query(value = """
    SELECT DISTINCT * 
    FROM supporter_shifts ss
    WHERE ss.shift_date = :date
      AND CAST(ss.start_time AS time) <= CAST(:slotStart AS time)
      AND CAST(ss.end_time AS time) >= CAST(:slotEnd AS time)
""", nativeQuery = true)
    List<SupporterShift> findAvailableShiftsForSlot(
            @Param("date") LocalDate date,
            @Param("slotStart") LocalTime slotStart,
            @Param("slotEnd") LocalTime slotEnd
    );


    @Query("SELECT s FROM SupporterShift s WHERE s.shiftDate = :date")
    List<SupporterShift> findByShiftDate(LocalDate date);

}
