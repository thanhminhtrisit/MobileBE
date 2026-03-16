package com.example.Back_end.service.impl;

import com.example.Back_end.dto.RoomEquipmentRequestDTO;
import com.example.Back_end.dto.RoomEquipmentResponseDTO;
import com.example.Back_end.entity.Room;
import com.example.Back_end.entity.RoomEquipment;
import com.example.Back_end.repository.RoomEquipmentRepository;
import com.example.Back_end.repository.RoomRepository;
import com.example.Back_end.service.interf.RoomEquipmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RoomEquipmentServiceImpl implements RoomEquipmentService {

    private final RoomEquipmentRepository roomEquipmentRepo;
    private final RoomRepository roomRepo;

    @Override
    public List<RoomEquipmentResponseDTO> getAll() {
        return roomEquipmentRepo.findAll()
                .stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    public RoomEquipmentResponseDTO getById(Long id) {
        RoomEquipment eq = roomEquipmentRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Room equipment not found"));
        return toResponseDTO(eq);
    }

    @Override
    public RoomEquipmentResponseDTO create(RoomEquipmentRequestDTO dto) {
        RoomEquipment eq = new RoomEquipment();
        eq.setEquipmentName(dto.getEquipmentName());
        eq.setEquipmentCode(dto.getEquipmentCode());
        eq.setCategory(dto.getCategory());
        eq.setStatus(dto.getStatus());

        if (dto.getRoomId() != null) {
            Room room = roomRepo.findById(dto.getRoomId())
                    .orElseThrow(() -> new RuntimeException("Room not found"));
            eq.setRoom(room);
        }

        return toResponseDTO(roomEquipmentRepo.save(eq));
    }

    @Override
    public RoomEquipmentResponseDTO update(Long id, RoomEquipmentRequestDTO dto) {
        RoomEquipment eq = roomEquipmentRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Room equipment not found"));
        eq.setEquipmentName(dto.getEquipmentName());
        eq.setEquipmentCode(dto.getEquipmentCode());
        eq.setCategory(dto.getCategory());
        eq.setStatus(dto.getStatus());

        if (dto.getRoomId() != null) {
            Room room = roomRepo.findById(dto.getRoomId())
                    .orElseThrow(() -> new RuntimeException("Room not found"));
            eq.setRoom(room);
        }

        return toResponseDTO(roomEquipmentRepo.save(eq));
    }

    @Override
    public void delete(Long id) {
        roomEquipmentRepo.deleteById(id);
    }

    private RoomEquipmentResponseDTO toResponseDTO(RoomEquipment eq) {
        RoomEquipmentResponseDTO dto = new RoomEquipmentResponseDTO();
        dto.setEquipmentId(eq.getEquipmentId());
        dto.setEquipmentName(eq.getEquipmentName());
        dto.setEquipmentCode(eq.getEquipmentCode());
        dto.setCategory(eq.getCategory());
        dto.setStatus(eq.getStatus());
        dto.setRoomName(eq.getRoom() != null ? eq.getRoom().getRoomName() : null);
        return dto;
    }
}
