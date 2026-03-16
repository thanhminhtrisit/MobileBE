package com.example.Back_end.service.impl;

import com.example.Back_end.dto.IncidentRequestDTO;
import com.example.Back_end.dto.IncidentResponseDTO;
import com.example.Back_end.entity.Incident;
import com.example.Back_end.repository.IncidentRepository;
import com.example.Back_end.repository.IncidentTypeRepository;
import com.example.Back_end.repository.LabRepository;
import com.example.Back_end.repository.SupporterRepository;
import com.example.Back_end.service.interf.IncidentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class IncidentServiceImpl implements IncidentService {

    private final IncidentRepository incidentRepo;
    private final IncidentTypeRepository incidentTypeRepo;
    private final SupporterRepository supporterRepo;
    private final LabRepository labRepo;

    @Override
    public List<IncidentResponseDTO> getAll() {
        return incidentRepo.findAll()
                .stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    public IncidentResponseDTO getById(Long id) {
        Incident incident = incidentRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Incident not found"));
        return toResponseDTO(incident);
    }

    @Override
    public IncidentResponseDTO create(IncidentRequestDTO dto) {
        Incident incident = new Incident();

        incident.setTitle(dto.getTitle());
        incident.setDescription(dto.getDescription());
        incident.setSeverity(dto.getSeverity());
        incident.setStatus(dto.getStatus());

        // ✅ Tự set thời gian khi tạo mới
        incident.setReportedAt(LocalDateTime.now());

        // ✅ Gắn khóa ngoại
        if (dto.getIncidentTypeId() != null) {
            incident.setIncidentType(incidentTypeRepo.findById(dto.getIncidentTypeId()).orElse(null));
        }
        if (dto.getSupporterId() != null) {
            incident.setSupporter(supporterRepo.findById(dto.getSupporterId()).orElse(null));
        }
        if (dto.getLabId() != null) {
            incident.setLab(labRepo.findById(dto.getLabId()).orElse(null));
        }

        return toResponseDTO(incidentRepo.save(incident));
    }

    @Override
    public IncidentResponseDTO update(Long id, IncidentRequestDTO dto) {
        Incident incident = incidentRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Incident not found"));

        incident.setTitle(dto.getTitle());
        incident.setDescription(dto.getDescription());
        incident.setSeverity(dto.getSeverity());

        // ✅ Nếu đổi trạng thái từ khác sang "resolved" thì set thời gian resolvedAt
        if (!"resolved".equalsIgnoreCase(incident.getStatus())
                && "resolved".equalsIgnoreCase(dto.getStatus())) {
            incident.setResolvedAt(LocalDateTime.now());
        }
        incident.setStatus(dto.getStatus());

        if (dto.getIncidentTypeId() != null) {
            incident.setIncidentType(incidentTypeRepo.findById(dto.getIncidentTypeId()).orElse(null));
        }
        if (dto.getSupporterId() != null) {
            incident.setSupporter(supporterRepo.findById(dto.getSupporterId()).orElse(null));
        }
        if (dto.getLabId() != null) {
            incident.setLab(labRepo.findById(dto.getLabId()).orElse(null));
        }

        return toResponseDTO(incidentRepo.save(incident));
    }

    @Override
    public void delete(Long id) {
        incidentRepo.deleteById(id);
    }

    private IncidentResponseDTO toResponseDTO(Incident incident) {
        IncidentResponseDTO dto = new IncidentResponseDTO();
        dto.setIncidentId(incident.getIncidentId());
        dto.setTitle(incident.getTitle());
        dto.setDescription(incident.getDescription());
        dto.setSeverity(incident.getSeverity());
        dto.setStatus(incident.getStatus());
        dto.setReportedAt(incident.getReportedAt());
        dto.setResolvedAt(incident.getResolvedAt());

        dto.setIncidentTypeName(
                incident.getIncidentType() != null ? incident.getIncidentType().getTypeName() : null
        );

        dto.setSupporterName(
                incident.getSupporter() != null && incident.getSupporter().getUser() != null
                        ? incident.getSupporter().getUser().getFullName()
                        : null
        );

        dto.setLabName(
                incident.getLab() != null ? incident.getLab().getLabName() : null
        );

        return dto;
    }

}
