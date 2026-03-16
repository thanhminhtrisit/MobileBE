package com.example.Back_end.service.impl;

import com.example.Back_end.dto.SupporterRequestDTO;
import com.example.Back_end.dto.SupporterResponseDTO;
import com.example.Back_end.dto.UserAssignDTO;
import com.example.Back_end.entity.Supporter;
import com.example.Back_end.entity.User;
import com.example.Back_end.repository.SupporterRepository;
import com.example.Back_end.repository.UserRepository;
import com.example.Back_end.service.interf.SupporterService;
import com.example.Back_end.validation.UserRoleValidator;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SupporterServiceImpl implements SupporterService {

    private final SupporterRepository supporterRepository;
    private final UserRepository userRepository;
    private final UserRoleValidator userRoleValidator;

    @Override
    public SupporterResponseDTO createSupporter(SupporterRequestDTO dto) {
        User user = userRepository.findById(dto.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        Supporter supporter = new Supporter();
        supporter.setUser(user);
        supporter.setSupporterCode(dto.getSupporterCode());
        supporter.setStatus(dto.getStatus());

        supporterRepository.save(supporter);
        return toDto(supporter);
    }

    @Override
    public SupporterResponseDTO getSupporterById(Long id) {
        Supporter supporter = supporterRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Supporter not found"));
        return toDto(supporter);
    }

    @Override
    public List<SupporterResponseDTO> getAllSupporters() {
        return supporterRepository.findAll().stream()
                .map(this::toDto).collect(Collectors.toList());
    }

    @Override
    public SupporterResponseDTO updateSupporter(Long id, SupporterRequestDTO dto) {
        Supporter supporter = supporterRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Supporter not found"));

        supporter.setSupporterCode(dto.getSupporterCode());
        supporter.setStatus(dto.getStatus());

        supporterRepository.save(supporter);
        return toDto(supporter);
    }

    @Override
    public void deleteSupporter(Long id) {
        supporterRepository.deleteById(id);
    }

    private SupporterResponseDTO toDto(Supporter supporter) {
        SupporterResponseDTO dto = new SupporterResponseDTO();
        dto.setSupporterId(supporter.getSupporterId());
        dto.setUserId(supporter.getUser().getUserId());
        dto.setSupporterCode(supporter.getSupporterCode());
        dto.setStatus(supporter.getStatus());
        dto.setRegisteredAt(supporter.getRegisteredAt());
        return dto;
    }

    @Transactional
    @Override
    public SupporterResponseDTO assignUser(UserAssignDTO request) {
        userRoleValidator.ensureUserHasNoOtherRole(request.getUserId(), "supporter");

        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        Supporter supporter = new Supporter();
        supporter.setUser(user);
        supporter.setSupporterCode("SUP-" + System.currentTimeMillis());
        supporter.setStatus("Active");
        supporter.setRegisteredAt(LocalDateTime.now());

        Supporter saved = supporterRepository.save(supporter);
        return toDto(saved);
    }

}