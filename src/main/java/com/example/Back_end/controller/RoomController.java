package com.example.Back_end.controller;

import com.example.Back_end.dto.RoomRequestDTO;
import com.example.Back_end.dto.RoomResponseDTO;
import com.example.Back_end.service.interf.RoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/rooms")
@RequiredArgsConstructor
public class RoomController {

    private final RoomService roomService;

    // ✅ Lấy tất cả phòng
    @GetMapping
    public ResponseEntity<List<RoomResponseDTO>> getAllRooms() {
        return ResponseEntity.ok(roomService.getAll());
    }

    // ✅ Lấy phòng theo ID
    @GetMapping("/{id}")
    public ResponseEntity<RoomResponseDTO> getRoomById(@PathVariable Long id) {
        return ResponseEntity.ok(roomService.getById(id));
    }

    // ✅ Tạo mới phòng
    @PostMapping
    public ResponseEntity<RoomResponseDTO> createRoom(@RequestBody RoomRequestDTO dto) {
        return ResponseEntity.ok(roomService.create(dto));
    }

    // ✅ Cập nhật phòng
    @PutMapping("/{id}")
    public ResponseEntity<RoomResponseDTO> updateRoom(
            @PathVariable Long id,
            @RequestBody RoomRequestDTO dto
    ) {
        return ResponseEntity.ok(roomService.update(id, dto));
    }

    // ✅ Xóa phòng
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteRoom(@PathVariable Long id) {
        roomService.delete(id);
        return ResponseEntity.ok("✅ Room with ID " + id + " has been deleted successfully.");
    }
}
