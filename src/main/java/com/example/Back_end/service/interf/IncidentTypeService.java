package com.example.Back_end.service.interf;

import com.example.Back_end.dto.IncidentTypeRequestDTO;
import com.example.Back_end.dto.IncidentTypeResponseDTO;

import java.util.List;

public interface IncidentTypeService {
    List<IncidentTypeResponseDTO> getAll();
    IncidentTypeResponseDTO getById(Long id);
    IncidentTypeResponseDTO create(IncidentTypeRequestDTO dto);
    IncidentTypeResponseDTO update(Long id, IncidentTypeRequestDTO dto);
    void delete(Long id);
}
