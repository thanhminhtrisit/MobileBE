package com.example.Back_end.controller;

import com.example.Back_end.dto.RequestRequestDTO;
import com.example.Back_end.dto.RequestResponseDTO;
import com.example.Back_end.service.interf.RequestService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/requests")
@RequiredArgsConstructor
public class RequestController {

    private final RequestService requestService;

    @GetMapping
    public ResponseEntity<List<RequestResponseDTO>> getAll() {
        return ResponseEntity.ok(requestService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<RequestResponseDTO> getById(@PathVariable Long id) {
        return ResponseEntity.ok(requestService.getById(id));
    }

    @PostMapping
    public ResponseEntity<RequestResponseDTO> create(@RequestBody RequestRequestDTO dto) {
        return ResponseEntity.ok(requestService.create(dto));
    }

    
    public ResponseEntity<RequestResponseDTO> update(@PathVariable Long id, @RequestBody RequestRequestDTO dto) {
        return ResponseEntity.ok(requestService.update(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        requestService.delete(id);
        return ResponseEntity.noContent().build();
    }



    @PutMapping("/{requestId}/approve/{staffId}")
    public ResponseEntity<RequestResponseDTO> approveByStaff(
            @PathVariable Long requestId,
            @PathVariable Long staffId
    ) {
        return ResponseEntity.ok(requestService.approveByStaff(staffId, requestId)); // ✅ Đúng thứ tự
    }

    @PutMapping("/{requestId}/reject/user/{userId}")
    public ResponseEntity<RequestResponseDTO> rejectByUser(
            @PathVariable Long requestId,
            @PathVariable Long userId
    ) {
        return ResponseEntity.ok(requestService.rejectByUser(userId, requestId));
    }


}
