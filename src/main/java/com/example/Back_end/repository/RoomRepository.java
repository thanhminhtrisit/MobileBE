package com.example.Back_end.repository;

import com.example.Back_end.entity.Lab;
import com.example.Back_end.entity.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoomRepository extends JpaRepository<Room, Long> {
    Optional<Room> findFirstByLabAndStatus(Lab lab, String status);

}
