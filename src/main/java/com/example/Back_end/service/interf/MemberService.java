package com.example.Back_end.service.interf;

import com.example.Back_end.dto.MemberRequestDTO;
import com.example.Back_end.dto.MemberResponseDTO;
import com.example.Back_end.dto.UserAssignDTO;

import java.util.List;

public interface MemberService {
    MemberResponseDTO create(MemberRequestDTO dto);
    MemberResponseDTO getById(Long id);
    List<MemberResponseDTO> getAll();
    MemberResponseDTO update(Long id, MemberRequestDTO dto);
    void delete(Long id);
    MemberResponseDTO assignUserToLab(UserAssignDTO request);
}
