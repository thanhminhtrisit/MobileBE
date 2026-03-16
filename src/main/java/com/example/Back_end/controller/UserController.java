// src/main/java/com/example/Back_end/controller/UserController.java

package com.example.Back_end.controller;


import com.example.Back_end.dto.UserRequestDT0;
import com.example.Back_end.dto.UserResponseDTO;
import com.example.Back_end.service.interf.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@Tag(name = "User Management", description = "APIs for managing users and admin roles")
public class UserController {

    private final UserService userService;

    // === CREATE USER ===
    @PostMapping
    @Operation(summary = "Create a new user", description = "Creates a user with provided details")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "User created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input")
    })
    public ResponseEntity<UserResponseDTO> createUser(@RequestBody UserRequestDT0 request) {
        return ResponseEntity.ok(userService.create(request));
    }

    // === UPDATE USER ===
    @PutMapping("/{id}")
    @Operation(summary = "Update user", description = "Updates user details by ID")
    public ResponseEntity<UserResponseDTO> updateUser(
            @PathVariable Long id,
            @RequestBody UserRequestDT0 request) {
        return ResponseEntity.ok(userService.update(id, request));
    }

    // === DELETE USER ===
    @DeleteMapping("/{id}")
    @Operation(summary = "Delete user", description = "Deletes a user by ID")
    @ApiResponse(responseCode = "204", description = "User deleted")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.delete(id);
        return ResponseEntity.noContent().build();
    }

    // === GET USER BY ID ===
    @GetMapping("/{id}")
    @Operation(summary = "Get user by ID")
    public ResponseEntity<UserResponseDTO> getUserById(@PathVariable Long id) {
        return ResponseEntity.ok(userService.getById(id));
    }

    // === GET ALL USERS ===
    @GetMapping
    @Operation(summary = "Get all users", description = "Returns list of all users")
    public ResponseEntity<List<UserResponseDTO>> getAllUsers() {
        return ResponseEntity.ok(userService.getAll());
    }

    // === SET ADMIN ROLE (GRANT / REVOKE) ===
    @PostMapping("/{id}/admin")
    public ResponseEntity<UserResponseDTO> setAdminRole(
            @PathVariable Long id,
            @RequestBody SetAdminRequest request) {
        return ResponseEntity.ok(userService.setAdmin(id, request.admin()));
    }

    @GetMapping("/{id}/admin")
    public ResponseEntity<AdminStatusResponse> checkAdminStatus(@PathVariable Long id) {
        UserResponseDTO user = userService.getById(id);
        return ResponseEntity.ok(new AdminStatusResponse(
                user.getUserId(),
                user.getUsername(),
                user.isAdmin()
        ));
    }

    // === DTO NHỎ GỌN CHO SET ADMIN ===
    record SetAdminRequest(boolean admin) {}

    // === DTO NHỎ GỌN CHO CHECK ADMIN ===
    record AdminStatusResponse(Long userId, String username, boolean admin) {}
}