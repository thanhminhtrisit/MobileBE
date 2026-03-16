package com.example.Back_end.service.impl;

import com.example.Back_end.dto.RoomRequestDTO;
import com.example.Back_end.dto.RoomResponseDTO;
import com.example.Back_end.entity.Lab;
import com.example.Back_end.entity.Room;
import com.example.Back_end.repository.LabRepository;
import com.example.Back_end.repository.RoomRepository;
import com.example.Back_end.service.interf.RoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RoomServiceImpl implements RoomService {

    private final RoomRepository roomRepo;
    private final LabRepository labRepo;

    @Override
    public List<RoomResponseDTO> getAll() {
        return roomRepo.findAll()
                .stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    public RoomResponseDTO getById(Long id) {
        Room room = roomRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Room not found"));
        return toResponseDTO(room);
    }

    @Override
    public RoomResponseDTO create(RoomRequestDTO dto) {
        Room room = new Room();
        room.setRoomName(dto.getRoomName());
        room.setRoomCode(dto.getRoomCode());
        room.setCapacity(dto.getCapacity());
        room.setStatus(dto.getStatus());

        if (dto.getLabId() != null) {
            Lab lab = labRepo.findById(dto.getLabId())
                    .orElseThrow(() -> new RuntimeException("Lab not found"));
            room.setLab(lab);
        }

        return toResponseDTO(roomRepo.save(room));
    }

    @Override
    public RoomResponseDTO update(Long id, RoomRequestDTO dto) {
        Room room = roomRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Room not found"));
        room.setRoomName(dto.getRoomName());
        room.setRoomCode(dto.getRoomCode());
        room.setCapacity(dto.getCapacity());
        room.setStatus(dto.getStatus());

        if (dto.getLabId() != null) {
            Lab lab = labRepo.findById(dto.getLabId())
                    .orElseThrow(() -> new RuntimeException("Lab not found"));
            room.setLab(lab);
        }

        return toResponseDTO(roomRepo.save(room));
    }

    @Override
    public void delete(Long id) {
        roomRepo.deleteById(id);
    }

    private RoomResponseDTO toResponseDTO(Room room) {
        RoomResponseDTO dto = new RoomResponseDTO();
        dto.setRoomId(room.getRoomId());
        dto.setRoomName(room.getRoomName());
        dto.setRoomCode(room.getRoomCode());
        dto.setCapacity(room.getCapacity());
        dto.setStatus(room.getStatus());
        dto.setLabName(room.getLab() != null ? room.getLab().getLabName() : null);
        return dto;
    }
}
