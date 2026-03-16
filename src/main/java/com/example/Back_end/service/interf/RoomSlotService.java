package com.example.Back_end.service.interf;

import com.example.Back_end.dto.RoomSlotRequestDTO;
import com.example.Back_end.dto.RoomSlotResponseDTO;
import com.example.Back_end.entity.RoomSlot;

import java.time.LocalDate;
import java.util.List;

public interface RoomSlotService {
    List<RoomSlotResponseDTO> getDistinctSlotTemplates();

    List<RoomSlotResponseDTO> getAll();

    RoomSlotResponseDTO getById(Long id);

    RoomSlotResponseDTO create(RoomSlotRequestDTO dto);

    RoomSlotResponseDTO update(Long id, RoomSlotRequestDTO dto);

    void delete(Long id);

    List<RoomSlotResponseDTO> getAvailableSlots();

    String generateRoomSlotsForYear(int year);

    String updateStatusByDate(LocalDate date, String status);
}
