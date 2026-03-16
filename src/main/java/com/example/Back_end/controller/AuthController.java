package com.example.Back_end.controller;

import com.example.Back_end.dto.GoogleSignInRequest;
import com.example.Back_end.dto.UserResponseDTO;
import com.example.Back_end.firebase.FirebaseAuthService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class AuthController {

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


}
