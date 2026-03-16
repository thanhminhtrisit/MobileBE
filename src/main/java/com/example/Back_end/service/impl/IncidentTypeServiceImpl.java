package com.example.Back_end.service.impl;

import com.example.Back_end.dto.IncidentTypeRequestDTO;
import com.example.Back_end.dto.IncidentTypeResponseDTO;
import com.example.Back_end.entity.IncidentType;
import com.example.Back_end.repository.IncidentTypeRepository;
import com.example.Back_end.service.interf.IncidentTypeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class IncidentTypeServiceImpl implements IncidentTypeService {

    private final IncidentTypeRepository incidentTypeRepository;

    @Override
    public List<IncidentTypeResponseDTO> getAll() {
        return incidentTypeRepository.findAll()
                .stream().map(this::toResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    public IncidentTypeResponseDTO getById(Long id) {
        IncidentType type = incidentTypeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("IncidentType not found"));
        return toResponseDTO(type);
    }

    @Override
    public IncidentTypeResponseDTO create(IncidentTypeRequestDTO dto) {
        IncidentType type = new IncidentType();
        type.setTypeName(dto.getTypeName());
        type.setDescription(dto.getDescription());
        type.setPriorityLevel(dto.getPriorityLevel());
        return toResponseDTO(incidentTypeRepository.save(type));
    }

    @Override
    public IncidentTypeResponseDTO update(Long id, IncidentTypeRequestDTO dto) {
        IncidentType type = incidentTypeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("IncidentType not found"));

        type.setTypeName(dto.getTypeName());
        type.setDescription(dto.getDescription());
        type.setPriorityLevel(dto.getPriorityLevel());

        return toResponseDTO(incidentTypeRepository.save(type));
    }

    @Override
    public void delete(Long id) {
        incidentTypeRepository.deleteById(id);
    }

    private IncidentTypeResponseDTO toResponseDTO(IncidentType type) {
        IncidentTypeResponseDTO dto = new IncidentTypeResponseDTO();
        dto.setIncidentTypeId(type.getIncidentTypeId());
        dto.setTypeName(type.getTypeName());
        dto.setDescription(type.getDescription());
        dto.setPriorityLevel(type.getPriorityLevel());
        return dto;
    }
}
