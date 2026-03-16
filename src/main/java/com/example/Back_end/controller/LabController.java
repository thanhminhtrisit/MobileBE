package com.example.Back_end.controller;

import com.example.Back_end.dto.LabAssignRoomSlotByDateDTO;
import com.example.Back_end.dto.LabRequestDTO;
import com.example.Back_end.dto.LabResponseDTO;
import com.example.Back_end.entity.RoomSlot;
import com.example.Back_end.enums.LabStatus;
import com.example.Back_end.service.interf.LabService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/labs")
@RequiredArgsConstructor
@Tag(name = "Lab Management", description = "APIs for managing laboratory information")
public class LabController {

    private final LabService labService;

    // === CREATE LAB ===
    @PostMapping
    @Operation(summary = "Create new lab", description = "Add a new laboratory to the system")
    public ResponseEntity<LabResponseDTO> createLab(@RequestBody LabRequestDTO request) {
        return ResponseEntity.ok(labService.create(request));
    }

    // === UPDATE LAB ===
    @PutMapping("/{id}")
    @Operation(summary = "Update lab", description = "Update lab information by ID")
    public ResponseEntity<LabResponseDTO> updateLab(
            @PathVariable Long id,
            @RequestBody LabRequestDTO request
    ) {
        return ResponseEntity.ok(labService.update(id, request));
    }

    // === GET ONE LAB ===
    @GetMapping("/{id}")
    @Operation(summary = "Get lab by ID")
    public ResponseEntity<LabResponseDTO> getLabById(@PathVariable Long id) {
        return ResponseEntity.ok(labService.getById(id));
    }

    // === GET ALL LABS ===
    @GetMapping
    @Operation(summary = "Get all labs")
    public ResponseEntity<List<LabResponseDTO>> getAllLabs() {
        return ResponseEntity.ok(labService.getAll());
    }

    // === DELETE LAB ===
    @DeleteMapping("/{id}")
    @Operation(summary = "Delete lab", description = "Remove a lab by its ID")
    public ResponseEntity<Void> deleteLab(@PathVariable Long id) {
        labService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/status")
    @Operation(summary = "Update lab status", description = "Change lab status (ACTIVE, INACTIVE, MAINTENANCE)")
    public ResponseEntity<LabResponseDTO> updateLabStatus(
            @PathVariable Long id,
            @RequestParam LabStatus status
    ) {
        return ResponseEntity.ok(labService.updateStatus(id, status));
    }

    @PostMapping("/assign-roomslots-by-date")
    public ResponseEntity<String> assignRoomSlotsByDate(@RequestBody LabAssignRoomSlotByDateDTO dto) {
        String message = labService.assignRoomSlotsToLabByDate(dto);
        return ResponseEntity.ok(message);
    }

    @GetMapping("/{labId}/room-slots")
    public ResponseEntity<List<RoomSlot>> getRoomSlotsByLab(@PathVariable Long labId) {
        List<RoomSlot> roomSlots = labService.getRoomSlotsByLabId(labId);
        return ResponseEntity.ok(roomSlots);
    }


}
