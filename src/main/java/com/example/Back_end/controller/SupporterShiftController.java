package com.example.Back_end.controller;

import com.example.Back_end.dto.SupporterShiftRequestDTO;
import com.example.Back_end.dto.SupporterShiftResponseDTO;
import com.example.Back_end.service.interf.SupporterShiftService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/supporter-shifts")
@RequiredArgsConstructor
public class SupporterShiftController {

    private final SupporterShiftService supporterShiftService;

    @GetMapping
    public ResponseEntity<List<SupporterShiftResponseDTO>> getAll() {
        return ResponseEntity.ok(supporterShiftService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<SupporterShiftResponseDTO> getById(@PathVariable Long id) {
        return ResponseEntity.ok(supporterShiftService.getById(id));
    }

    @PostMapping
    public ResponseEntity<SupporterShiftResponseDTO> create(@RequestBody SupporterShiftRequestDTO dto) {
        return ResponseEntity.ok(supporterShiftService.create(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<SupporterShiftResponseDTO> update(@PathVariable Long id, @RequestBody SupporterShiftRequestDTO dto) {
        return ResponseEntity.ok(supporterShiftService.update(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        supporterShiftService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
