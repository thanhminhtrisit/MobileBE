package com.example.Back_end.service.interf;

import com.example.Back_end.dto.LabAssignRoomSlotByDateDTO;
import com.example.Back_end.dto.LabRequestDTO;
import com.example.Back_end.dto.LabResponseDTO;
import com.example.Back_end.entity.RoomSlot;
import com.example.Back_end.enums.LabStatus;

import java.util.List;

public interface LabService {
    LabResponseDTO create(LabRequestDTO request);
    LabResponseDTO update(Long labId, LabRequestDTO request);
    LabResponseDTO getById(Long labId);
    List<LabResponseDTO> getAll();
    void delete(Long labId);
    LabResponseDTO updateStatus(Long id, LabStatus status);

    String assignRoomSlotsToLabByDate(LabAssignRoomSlotByDateDTO dto);

    List<RoomSlot> getRoomSlotsByLabId(Long labId);

}
