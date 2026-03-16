package com.example.Back_end.service.interf;

import com.example.Back_end.dto.UserRequestDT0;
import com.example.Back_end.dto.UserResponseDTO;

import java.util.List;

public interface UserService {
    UserResponseDTO create(UserRequestDT0 request);
    UserResponseDTO update(Long id, UserRequestDT0 request);
    void delete(Long id);
    UserResponseDTO getById(Long id);
    List<UserResponseDTO> getAll();
    UserResponseDTO setAdmin(Long id, boolean isAdmin);
}
