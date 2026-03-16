package com.example.Back_end.service.interf;

import com.example.Back_end.dto.IncidentRequestDTO;
import com.example.Back_end.dto.IncidentResponseDTO;

import java.util.List;

public interface IncidentService {
    List<IncidentResponseDTO> getAll();
    IncidentResponseDTO getById(Long id);
    IncidentResponseDTO create(IncidentRequestDTO dto);
    IncidentResponseDTO update(Long id, IncidentRequestDTO dto);
    void delete(Long id);
}
