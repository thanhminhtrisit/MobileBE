package com.example.Back_end.service.impl;

import com.example.Back_end.dto.StaffRequestDTO;
import com.example.Back_end.dto.StaffResponseDTO;
import com.example.Back_end.dto.UserAssignDTO;
import com.example.Back_end.entity.Lab;
import com.example.Back_end.entity.Staff;
import com.example.Back_end.entity.User;
import com.example.Back_end.repository.LabRepository;
import com.example.Back_end.repository.StaffRepository;
import com.example.Back_end.repository.UserRepository;
import com.example.Back_end.service.interf.StaffService;
import com.example.Back_end.validation.UserRoleValidator;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class StaffServiceImpl implements StaffService {

    private final StaffRepository staffRepository;
    private final UserRepository userRepository;
    private final LabRepository labRepository;
    private final UserRoleValidator userRoleValidator;

    @Override
    public StaffResponseDTO create(StaffRequestDTO dto) {
        User user = userRepository.findById(dto.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));
        Lab lab = labRepository.findById(dto.getLabId())
                .orElseThrow(() -> new RuntimeException("Lab not found"));

        Staff staff = new Staff();
        staff.setUser(user);
        staff.setLabs(List.of(lab));
        staff.setStaffCode(dto.getStaffCode());
        staff.setPosition(dto.getPosition());
        staff.setDepartment(dto.getDepartment());
        staff.setHiredAt(LocalDateTime.now());

        staffRepository.save(staff);
        return toDto(staff);
    }

    @Override
    public StaffResponseDTO getById(Long id) {
        Staff staff = staffRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Staff not found"));
        return toDto(staff);
    }

    @Override
    public List<StaffResponseDTO> getAll() {
        return staffRepository.findAll().stream()
                .map(this::toDto)
                .toList();
    }

    @Override
    public StaffResponseDTO update(Long id, StaffRequestDTO dto) {
        Staff staff = staffRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Staff not found"));

        if (dto.getLabId() != null) {
            Lab lab = labRepository.findById(dto.getLabId())
                    .orElseThrow(() -> new RuntimeException("Lab not found"));
            staff.setLabs(List.of(lab));
        }

        staff.setStaffCode(dto.getStaffCode());
        staff.setPosition(dto.getPosition());
        staff.setDepartment(dto.getDepartment());

        staffRepository.save(staff);
        return toDto(staff);
    }

    @Override
    public void delete(Long id) {
        staffRepository.deleteById(id);
    }

    @Transactional
    @Override
    public StaffResponseDTO assignUserToLab(UserAssignDTO request) {
        userRoleValidator.ensureUserHasNoOtherRole(request.getUserId(), "staff");

        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));
        Lab lab = labRepository.findById(request.getLabId())
                .orElseThrow(() -> new RuntimeException("Lab not found"));

        Staff staff = new Staff();
        staff.setUser(user);
        staff.setLabs(List.of(lab));
        staff.setStaffCode("STF-" + System.currentTimeMillis());
        staff.setPosition("Assistant");
        staff.setDepartment("General");
        staff.setHiredAt(LocalDateTime.now());

        Staff saved = staffRepository.save(staff);
        return toDto(saved);
    }


    private StaffResponseDTO toDto(Staff staff) {
        StaffResponseDTO dto = new StaffResponseDTO();
        dto.setStaffId(staff.getStaffId());
        dto.setUserId(staff.getUser().getUserId());
        dto.setLabIds(
                staff.getLabs() != null
                        ? staff.getLabs().stream().map(Lab::getLabId).toList()
                        : List.of()
        );
        dto.setStaffCode(staff.getStaffCode());
        dto.setPosition(staff.getPosition());
        dto.setDepartment(staff.getDepartment());
        dto.setHiredAt(staff.getHiredAt());
        return dto;
    }
}

