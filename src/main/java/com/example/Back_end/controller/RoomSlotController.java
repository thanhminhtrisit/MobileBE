package com.example.Back_end.controller;

import com.example.Back_end.dto.RoomSlotResponseDTO;
import com.example.Back_end.entity.RoomSlot;
import com.example.Back_end.service.interf.RoomSlotService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/room-slots")
@RequiredArgsConstructor
public class RoomSlotController {

    private final RoomSlotService roomSlotService;

    @GetMapping("/templates")
    public ResponseEntity<List<RoomSlotResponseDTO>> getDistinctSlotTemplates() {
        return ResponseEntity.ok(roomSlotService.getDistinctSlotTemplates());
    }

    @PostMapping("/generate/{year}")
    public ResponseEntity<String> generateRoomSlots(@PathVariable int year) {
        String message = roomSlotService.generateRoomSlotsForYear(year);
        return ResponseEntity.ok(message);
    }

    @PutMapping("/update-status/{date}")
    public ResponseEntity<String> updateStatusByDate(
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            @RequestParam String status
    ) {
        String message = roomSlotService.updateStatusByDate(date, status);
        return ResponseEntity.ok(message);
    }
}
