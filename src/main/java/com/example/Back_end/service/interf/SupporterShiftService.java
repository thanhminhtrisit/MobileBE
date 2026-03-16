package com.example.Back_end.service.interf;

import com.example.Back_end.dto.SupporterShiftRequestDTO;
import com.example.Back_end.dto.SupporterShiftResponseDTO;

import java.util.List;

public interface SupporterShiftService {
    List<SupporterShiftResponseDTO> getAll();
    SupporterShiftResponseDTO getById(Long id);
    SupporterShiftResponseDTO create(SupporterShiftRequestDTO dto);
    SupporterShiftResponseDTO update(Long id, SupporterShiftRequestDTO dto);
    void delete(Long id);
}
