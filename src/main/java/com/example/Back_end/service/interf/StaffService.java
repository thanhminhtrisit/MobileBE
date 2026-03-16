package com.example.Back_end.service.interf;

import com.example.Back_end.dto.StaffRequestDTO;
import com.example.Back_end.dto.StaffResponseDTO;
import com.example.Back_end.dto.UserAssignDTO;

import java.util.List;

public interface StaffService {
    StaffResponseDTO create(StaffRequestDTO dto);
    StaffResponseDTO getById(Long id);
    List<StaffResponseDTO> getAll();
    StaffResponseDTO update(Long id, StaffRequestDTO dto);
    void delete(Long id);
    StaffResponseDTO assignUserToLab(UserAssignDTO request);
}
