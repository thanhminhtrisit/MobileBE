package com.example.Back_end.service.impl;

import com.example.Back_end.dto.LabAssignRoomSlotByDateDTO;
import com.example.Back_end.dto.LabRequestDTO;
import com.example.Back_end.dto.LabResponseDTO;
import com.example.Back_end.entity.Lab;
import com.example.Back_end.entity.RoomSlot;
import com.example.Back_end.enums.LabStatus;
import com.example.Back_end.repository.LabRepository;
import com.example.Back_end.repository.RoomRepository;
import com.example.Back_end.repository.RoomSlotRepository;
import com.example.Back_end.service.interf.LabService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LabServiceImpl implements LabService {

    private final LabRepository labRepository;
    private final RoomRepository roomRepository;
    private final RoomSlotRepository roomSlotRepository;

    @Override
    public LabResponseDTO create(LabRequestDTO request) {
        Lab lab = new Lab();
        lab.setLabName(request.getLabName());
        lab.setLabCode(request.getLabCode());
        lab.setLocation(request.getLocation());
        lab.setDescription(request.getDescription());
        lab.setStatus(LabStatus.ACTIVE); // Mặc định là ACTIVE

        Lab saved = labRepository.save(lab);
        return mapToResponse(saved);
    }

    @Override
    public LabResponseDTO update(Long labId, LabRequestDTO request) {
        Lab lab = labRepository.findById(labId)
                .orElseThrow(() -> new RuntimeException("Lab not found with ID: " + labId));

        lab.setLabName(request.getLabName());
        lab.setLabCode(request.getLabCode());
        lab.setLocation(request.getLocation());
        lab.setDescription(request.getDescription());

        Lab updated = labRepository.save(lab);
        return mapToResponse(updated);
    }

    @Override
    public LabResponseDTO getById(Long labId) {
        Lab lab = labRepository.findById(labId)
                .orElseThrow(() -> new RuntimeException("Lab not found with ID: " + labId));
        return mapToResponse(lab);
    }

    @Override
    public List<LabResponseDTO> getAll() {
        return labRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public void delete(Long labId) {
        if (!labRepository.existsById(labId)) {
            throw new RuntimeException("Lab not found with ID: " + labId);
        }
        labRepository.deleteById(labId);
    }

    // Hàm map entity -> response DTO
    private LabResponseDTO mapToResponse(Lab lab) {
        LabResponseDTO res = new LabResponseDTO();
        res.setLabId(lab.getLabId());
        res.setLabName(lab.getLabName());
        res.setLabCode(lab.getLabCode());
        res.setLocation(lab.getLocation());
        res.setDescription(lab.getDescription());
        res.setStatus(lab.getStatus().name());
        return res;
    }

    @Override
    public LabResponseDTO updateStatus(Long id, LabStatus status) {
        Lab lab = labRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Lab not found with ID: " + id));

        lab.setStatus(status);
        Lab updated = labRepository.save(lab);

        return mapToResponse(updated);
    }

    @Override
    @Transactional
    public String assignRoomSlotsToLabByDate(LabAssignRoomSlotByDateDTO dto) {
        Lab lab = labRepository.findById(dto.getLabId())
                .orElseThrow(() -> new RuntimeException("❌ Không tìm thấy Lab có ID: " + dto.getLabId()));

        // Lấy tất cả RoomSlot theo ngày
        List<RoomSlot> roomSlotsInDay = roomSlotRepository.findByBookingDate(dto.getBookingDate());

        if (roomSlotsInDay.isEmpty()) {
            throw new RuntimeException("⚠️ Không tìm thấy RoomSlot nào cho ngày " + dto.getBookingDate());
        }

        // Lọc theo danh sách ca được chọn
        List<RoomSlot> selectedSlots = roomSlotsInDay.stream()
                .filter(rs -> dto.getSlotNames().contains(rs.getSlotName()))
                .toList();

        if (selectedSlots.isEmpty()) {
            throw new RuntimeException("⚠️ Không tìm thấy RoomSlot nào trùng với các ca: " + dto.getSlotNames());
        }

        // Gán vào Lab
        lab.getRoomSlots().addAll(selectedSlots);
        labRepository.save(lab);

        return "✅ Đã gán " + selectedSlots.size() + " RoomSlot (" + dto.getSlotNames() +
                ") vào Lab: " + lab.getLabName() + " cho ngày " + dto.getBookingDate();
    }

    @Override
    public List<RoomSlot> getRoomSlotsByLabId(Long labId) {
        Lab lab = labRepository.findById(labId)
                .orElseThrow(() -> new RuntimeException("❌ Không tìm thấy Lab có ID: " + labId));

        List<RoomSlot> roomSlots = lab.getRoomSlots();

        if (roomSlots == null || roomSlots.isEmpty()) {
            throw new RuntimeException("⚠️ Lab này hiện chưa có RoomSlot nào được gán.");
        }

        return roomSlots;
    }


}
