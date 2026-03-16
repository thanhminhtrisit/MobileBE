package com.example.Back_end.service.interf;

import com.example.Back_end.dto.RoomEquipmentRequestDTO;
import com.example.Back_end.dto.RoomEquipmentResponseDTO;

import java.util.List;

public interface RoomEquipmentService {
    List<RoomEquipmentResponseDTO> getAll();
    RoomEquipmentResponseDTO getById(Long id);
    RoomEquipmentResponseDTO create(RoomEquipmentRequestDTO dto);
    RoomEquipmentResponseDTO update(Long id, RoomEquipmentRequestDTO dto);
    void delete(Long id);
}
