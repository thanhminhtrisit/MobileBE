package com.example.Back_end.service.interf;

import com.example.Back_end.dto.RoomRequestDTO;
import com.example.Back_end.dto.RoomResponseDTO;

import java.util.List;

public interface RoomService {
    List<RoomResponseDTO> getAll();
    RoomResponseDTO getById(Long id);
    RoomResponseDTO create(RoomRequestDTO dto);
    RoomResponseDTO update(Long id, RoomRequestDTO dto);
    void delete(Long id);
}
