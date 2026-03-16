package com.example.Back_end.controller;

import com.example.Back_end.dto.IncidentRequestDTO;
import com.example.Back_end.dto.IncidentResponseDTO;
import com.example.Back_end.service.interf.IncidentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/incidents")
@RequiredArgsConstructor
public class IncidentController {


    private final IncidentService incidentService;

    @GetMapping
    public ResponseEntity<List<IncidentResponseDTO>> getAll() {
        return ResponseEntity.ok(incidentService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<IncidentResponseDTO> getById(@PathVariable Long id) {
        return ResponseEntity.ok(incidentService.getById(id));
    }

    @PostMapping
    public ResponseEntity<IncidentResponseDTO> create(@RequestBody IncidentRequestDTO dto) {
        return ResponseEntity.ok(incidentService.create(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<IncidentResponseDTO> update(@PathVariable Long id, @RequestBody IncidentRequestDTO dto) {
        return ResponseEntity.ok(incidentService.update(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        incidentService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
