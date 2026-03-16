package com.example.Back_end.controller;

import com.example.Back_end.dto.SupporterRequestDTO;
import com.example.Back_end.dto.SupporterResponseDTO;
import com.example.Back_end.service.interf.SupporterService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/supporters")
@RequiredArgsConstructor
public class SupporterController {

    private final SupporterService supporterService;

    @PostMapping
    public SupporterResponseDTO create(@RequestBody SupporterRequestDTO dto) {
        return supporterService.createSupporter(dto);
    }

    @GetMapping("/{id}")
    public SupporterResponseDTO getById(@PathVariable Long id) {
        return supporterService.getSupporterById(id);
    }

    @GetMapping
    public List<SupporterResponseDTO> getAll() {
        return supporterService.getAllSupporters();
    }

    @PutMapping("/{id}")
    public SupporterResponseDTO update(@PathVariable Long id, @RequestBody SupporterRequestDTO dto) {
        return supporterService.updateSupporter(id, dto);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        supporterService.deleteSupporter(id);
    }
}
