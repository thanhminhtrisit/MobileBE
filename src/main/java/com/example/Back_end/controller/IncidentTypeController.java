package com.example.Back_end.controller;

import com.example.Back_end.dto.IncidentTypeRequestDTO;
import com.example.Back_end.dto.IncidentTypeResponseDTO;
import com.example.Back_end.service.interf.IncidentTypeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/incident-types")
@RequiredArgsConstructor
public class IncidentTypeController {

    private final IncidentTypeService incidentTypeService;

    @GetMapping
    public ResponseEntity<List<IncidentTypeResponseDTO>> getAll() {
        return ResponseEntity.ok(incidentTypeService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<IncidentTypeResponseDTO> getById(@PathVariable Long id) {
        return ResponseEntity.ok(incidentTypeService.getById(id));
    }

    @PostMapping
    public ResponseEntity<IncidentTypeResponseDTO> create(@RequestBody IncidentTypeRequestDTO dto) {
        return ResponseEntity.ok(incidentTypeService.create(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<IncidentTypeResponseDTO> update(@PathVariable Long id,
                                                          @RequestBody IncidentTypeRequestDTO dto) {
        return ResponseEntity.ok(incidentTypeService.update(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        incidentTypeService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
