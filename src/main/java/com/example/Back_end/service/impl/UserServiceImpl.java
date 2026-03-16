package com.example.Back_end.service.impl;

import com.example.Back_end.dto.UserRequestDT0;
import com.example.Back_end.dto.UserResponseDTO;
import com.example.Back_end.entity.User;
import com.example.Back_end.repository.UserRepository;
import com.example.Back_end.service.interf.UserService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.modelmapper.ModelMapper;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final ModelMapper modelMapper = new ModelMapper();
    @Override
    public UserResponseDTO create(UserRequestDT0 request) {
        User user = modelMapper.map(request, User.class);
        user.setFirebaseUid("temp-" + System.currentTimeMillis());
        user.setCreatedAt(LocalDateTime.now());
        user.setUpdatedAt(LocalDateTime.now());
        return modelMapper.map(userRepository.save(user), UserResponseDTO.class);
    }

    @Override
    public UserResponseDTO update(Long id, UserRequestDT0 request) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (isNotBlank(request.getUsername())) user.setUsername(request.getUsername());
        if (isNotBlank(request.getFullName())) user.setFullName(request.getFullName());
        if (isNotBlank(request.getPhone())) user.setPhone(request.getPhone());

        if (request.getAdmin() != null) {  // ✅ tránh ghi đè nếu client không gửi
            user.setAdmin(request.getAdmin());
        }

        user.setUpdatedAt(LocalDateTime.now());
        return modelMapper.map(userRepository.save(user), UserResponseDTO.class);
    }


    @Override
    public UserResponseDTO setAdmin(Long id, boolean isAdmin) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        System.out.println("TRƯỚC: admin = " + user.isAdmin());

        user.setAdmin(isAdmin);
        user.setUpdatedAt(LocalDateTime.now());

        User saved = userRepository.save(user);

        System.out.println("SAU: admin = " + saved.isAdmin());

        return modelMapper.map(saved, UserResponseDTO.class);
    }

    @Transactional
    @Override
    public void delete(Long id) {
        userRepository.deleteById(id);
    }
    @Override
    public UserResponseDTO getById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        UserResponseDTO dto = modelMapper.map(user, UserResponseDTO.class);

        // ✅ Xác định role dựa vào quan hệ
        if (user.getStaff() != null) {
            dto.setRole("STAFF");
            dto.setStaffId(user.getStaff().getStaffId());
        } else if (user.getSupporter() != null) {
            dto.setRole("SUPPORTER");
        } else if (user.getMember() != null) {
            dto.setRole("MEMBER");
        } else {
            dto.setRole("UNKNOWN");
        }

        return dto;
    }
    @Override
    public List<UserResponseDTO> getAll() {
        return userRepository.findAll().stream()
                .map(user -> {
                    UserResponseDTO dto = modelMapper.map(user, UserResponseDTO.class);
                    if (user.getStaff() != null) dto.setRole("STAFF");
                    else if (user.getSupporter() != null) dto.setRole("SUPPORTER");
                    else if (user.getMember() != null) dto.setRole("MEMBER");
                    else dto.setRole("UNKNOWN");
                    return dto;
                })
                .collect(Collectors.toList());
    }

    // === HÀM HỖ TRỢ: KIỂM TRA CHUỖI KHÔNG NULL & KHÔNG RỖNG ===
    private boolean isNotBlank(String str) {
        return str != null && !str.trim().isEmpty();
    }
}