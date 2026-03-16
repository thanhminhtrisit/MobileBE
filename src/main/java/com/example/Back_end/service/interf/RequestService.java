package com.example.Back_end.service.interf;

import com.example.Back_end.dto.RequestRequestDTO;
import com.example.Back_end.dto.RequestResponseDTO;

import java.util.List;

public interface RequestService {
    List<RequestResponseDTO> getAll();
    RequestResponseDTO getById(Long id);
    RequestResponseDTO create(RequestRequestDTO dto);
    RequestResponseDTO update(Long id, RequestRequestDTO dto);
    void delete(Long id);
    public RequestResponseDTO approveByStaff(Long requestId, Long staffId);
    RequestResponseDTO rejectByUser(Long userId, Long requestId);

}