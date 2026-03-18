package com.example.Back_end.controller;

import com.example.Back_end.dto.*;
import com.example.Back_end.entity.User;
import com.example.Back_end.enums.Role;
import com.example.Back_end.firebase.FirebaseAuthService;
import com.example.Back_end.repository.UserRepository;
import com.example.Back_end.service.JwtService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class AuthController {
    @Autowired
    private JwtService jwtService;
    @Autowired
    private UserRepository userRepository;   // giả sử bạn đã có
    @Autowired
    private PasswordEncoder passwordEncoder; // sẽ tạo bean

    private final FirebaseAuthService authService;

    @PostMapping("/signin")
    public ResponseEntity<UserResponseDTO> signIn(
            @RequestBody GoogleSignInRequest request,
            HttpServletResponse response) {

        UserResponseDTO userResponse = authService.signInWithGoogle(request);

        response.addHeader("Set-Cookie",
                "idToken=" + request.idToken() +
                        "; Path=/; HttpOnly; SameSite=Lax; Max-Age=3600");

        return ResponseEntity.ok(userResponse);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new RuntimeException("Invalid password");
        }

        String token = jwtService.generateToken(user.getUserId(), user.getEmail(), user.getRole());

        AuthResponse response = new AuthResponse();
        response.setToken(token);
        response.setUserId(user.getUserId());
        response.setFullName(user.getFullName());
        response.setEmail(user.getEmail());
        response.setRole(user.getRole());

        return ResponseEntity.ok(response);
    }

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@RequestBody RegisterWithoutGGRequest request) {
        // Kiểm tra trùng
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email đã tồn tại!");
        }
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new RuntimeException("Username đã tồn tại!");
        }

        User user = new User();
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setFullName(request.getFullName());
        user.setPhone(request.getPhone());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(Role.MEMBER);           // mặc định MEMBER
        user.setAdmin(false);
        user.setFirebaseUid("local_" + request.getEmail());  // xử lý unique constraint

        User savedUser = userRepository.save(user);

        // Tự động login sau khi register
        String token = jwtService.generateToken(savedUser.getUserId(), savedUser.getEmail(), savedUser.getRole());

        AuthResponse response = new AuthResponse();
        response.setToken(token);
        response.setUserId(savedUser.getUserId());
        response.setFullName(savedUser.getFullName());
        response.setEmail(savedUser.getEmail());
        response.setRole(savedUser.getRole());

        return ResponseEntity.ok(response);
    }

}
