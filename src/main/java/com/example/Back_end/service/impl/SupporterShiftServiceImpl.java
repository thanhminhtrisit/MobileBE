package com.example.Back_end.service.impl;

import com.example.Back_end.dto.SupporterShiftRequestDTO;
import com.example.Back_end.dto.SupporterShiftResponseDTO;
import com.example.Back_end.entity.SupporterShift;
import com.example.Back_end.entity.Supporter;
import com.example.Back_end.repository.SupporterRepository;
import com.example.Back_end.repository.SupporterShiftRepository;
import com.example.Back_end.service.interf.SupporterShiftService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SupporterShiftServiceImpl implements SupporterShiftService {

    private final SupporterShiftRepository supporterShiftRepo;
    private final SupporterRepository supporterRepo;

    @Override
    public List<SupporterShiftResponseDTO> getAll() {
        return supporterShiftRepo.findAll().stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public SupporterShiftResponseDTO getById(Long id) {
        return supporterShiftRepo.findById(id)
                .map(this::toResponse)
                .orElseThrow(() -> new RuntimeException("SupporterShift not found"));
    }

    @Override
    public SupporterShiftResponseDTO create(SupporterShiftRequestDTO dto) {
        SupporterShift shift = new SupporterShift();
        shift.setShiftDate(dto.getShiftDate());
        shift.setStartTime(dto.getStartTime());
        shift.setEndTime(dto.getEndTime());

        if (dto.getSupporterIds() != null && !dto.getSupporterIds().isEmpty()) {
            shift.setSupporters(
                    dto.getSupporterIds().stream()
                            .map(id -> supporterRepo.findById(id)
                                    .orElseThrow(() -> new RuntimeException("Supporter not found: " + id)))
                            .collect(Collectors.toList())
            );
        }

        return toResponse(supporterShiftRepo.save(shift));
    }

    @Override
    public SupporterShiftResponseDTO update(Long id, SupporterShiftRequestDTO dto) {
        SupporterShift shift = supporterShiftRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("SupporterShift not found"));

        shift.setShiftDate(dto.getShiftDate());
        shift.setStartTime(dto.getStartTime());
        shift.setEndTime(dto.getEndTime());

        if (dto.getSupporterIds() != null) {
            shift.setSupporters(
                    dto.getSupporterIds().stream()
                            .map(supporterId -> supporterRepo.findById(supporterId)
                                    .orElseThrow(() -> new RuntimeException("Supporter not found: " + supporterId)))
                            .collect(Collectors.toList())
            );
        }

        return toResponse(supporterShiftRepo.save(shift));
    }

    @Override
    public void delete(Long id) {
        supporterShiftRepo.deleteById(id);
    }

    private SupporterShiftResponseDTO toResponse(SupporterShift shift) {
        SupporterShiftResponseDTO dto = new SupporterShiftResponseDTO();
        dto.setSupporterShiftId(shift.getSupporterShiftId());
        dto.setShiftDate(shift.getShiftDate());
        dto.setStartTime(shift.getStartTime());
        dto.setEndTime(shift.getEndTime());
        dto.setSupporterCodes(
                shift.getSupporters() != null
                        ? shift.getSupporters().stream()
                        .map(Supporter::getSupporterCode)
                        .collect(Collectors.toList())
                        : null
        );
        return dto;
    }
}
