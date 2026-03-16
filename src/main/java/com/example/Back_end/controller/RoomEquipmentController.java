package com.example.Back_end.controller;

import com.example.Back_end.dto.RoomEquipmentRequestDTO;
import com.example.Back_end.dto.RoomEquipmentResponseDTO;
import com.example.Back_end.service.interf.RoomEquipmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/room-equipments")
@RequiredArgsConstructor
public class RoomEquipmentController {

    private final RoomEquipmentService roomEquipmentService;

    @GetMapping
    public ResponseEntity<List<RoomEquipmentResponseDTO>> getAll() {
        return ResponseEntity.ok(roomEquipmentService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<RoomEquipmentResponseDTO> getById(@PathVariable Long id) {
        return ResponseEntity.ok(roomEquipmentService.getById(id));
    }

    @PostMapping
    public ResponseEntity<RoomEquipmentResponseDTO> create(@RequestBody RoomEquipmentRequestDTO dto) {
        return ResponseEntity.ok(roomEquipmentService.create(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<RoomEquipmentResponseDTO> update(@PathVariable Long id, @RequestBody RoomEquipmentRequestDTO dto) {
        return ResponseEntity.ok(roomEquipmentService.update(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        roomEquipmentService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
