package com.example.Back_end.controller;

import com.example.Back_end.dto.StaffRequestDTO;
import com.example.Back_end.dto.StaffResponseDTO;
import com.example.Back_end.service.interf.StaffService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/staff")
@RequiredArgsConstructor
public class StaffController {

    private final StaffService staffService;

    @PostMapping
    public StaffResponseDTO create(@RequestBody StaffRequestDTO dto) {
        return staffService.create(dto);
    }

    @GetMapping("/{id}")
    public StaffResponseDTO get(@PathVariable Long id) {
        return staffService.getById(id);
    }

    @GetMapping
    public List<StaffResponseDTO> getAll() {
        return staffService.getAll();
    }

    @PutMapping("/{id}")
    public StaffResponseDTO update(@PathVariable Long id, @RequestBody StaffRequestDTO dto) {
        return staffService.update(id, dto);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        staffService.delete(id);
    }
}

