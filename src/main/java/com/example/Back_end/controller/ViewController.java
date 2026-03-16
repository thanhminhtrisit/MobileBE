// src/main/java/com/example/Back_end/controller/ViewController.java
package com.example.Back_end.controller;

import com.example.Back_end.dto.DashboardUser;
import com.example.Back_end.entity.User;
import com.example.Back_end.repository.UserRepository;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseToken;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Arrays;
import java.util.Objects;

@Controller
@RequiredArgsConstructor
public class ViewController {

    private final UserRepository userRepository;
    private final FirebaseAuth firebaseAuth;

    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }

    @GetMapping("/dashboard")
    public String dashboard(
            @AuthenticationPrincipal OAuth2User principal,
            HttpServletRequest request,
            Model model) {

        if (principal == null || extractTokenFromCookie(request) == null) {
            return "redirect:/login";
        }

        String firebaseUid = principal.getAttribute("uid");
        String idToken = extractTokenFromCookie(request);

        try {
            FirebaseToken decodedToken = firebaseAuth.verifyIdToken(idToken);
            if (!firebaseUid.equals(decodedToken.getUid())) {
                return "redirect:/login";
            }

            User dbUser = userRepository.findByFirebaseUid(firebaseUid)
                    .orElseGet(() -> createUserFromToken(decodedToken));

            updateUserIfNeeded(dbUser, decodedToken);
            userRepository.save(dbUser);

            // KIỂM TRA ADMIN
            if (!dbUser.isAdmin()) {
                return "redirect:/access-denied"; // TRANG CẤM TRUY CẬP
            }

            // Chỉ admin mới vào được
            DashboardUser dashboardUser = DashboardUser.builder()
                    .userId(dbUser.getUserId())
                    .username(dbUser.getUsername())
                    .email(dbUser.getEmail())
                    .displayName(dbUser.getFullName())
                    .phone(dbUser.getPhone())
                    .uid(firebaseUid)
                    .createdAt(dbUser.getCreatedAt())
                    .updatedAt(dbUser.getUpdatedAt())
                    .photoURL(decodedToken.getPicture())
                    .emailVerified(decodedToken.isEmailVerified())
                    .providerId("google.com")
                    .build();

            model.addAttribute("user", dashboardUser);
            return "dashboard";

        } catch (Exception e) {
            return "redirect:/login";
        }
    }

    @GetMapping("/profile")
    public String profile(
            @AuthenticationPrincipal OAuth2User principal,
            HttpServletRequest request,
            Model model) {

        if (principal == null || extractTokenFromCookie(request) == null) {
            return "redirect:/login";
        }

        String firebaseUid = principal.getAttribute("uid");
        String idToken = extractTokenFromCookie(request);

        try {
            FirebaseToken decodedToken = firebaseAuth.verifyIdToken(idToken);
            if (!firebaseUid.equals(decodedToken.getUid())) {
                return "redirect:/login";
            }

            User dbUser = userRepository.findByFirebaseUid(firebaseUid)
                    .orElseGet(() -> createUserFromToken(decodedToken));

            updateUserIfNeeded(dbUser, decodedToken);
            userRepository.save(dbUser);

            DashboardUser profileUser = DashboardUser.builder()
                    .userId(dbUser.getUserId())
                    .username(dbUser.getUsername())
                    .email(dbUser.getEmail())
                    .displayName(dbUser.getFullName())
                    .phone(dbUser.getPhone())
                    .uid(firebaseUid)
                    .createdAt(dbUser.getCreatedAt())
                    .updatedAt(dbUser.getUpdatedAt())
                    .photoURL(decodedToken.getPicture())
                    .emailVerified(decodedToken.isEmailVerified())
                    .providerId("google.com")
                    .build();

            model.addAttribute("user", profileUser);
            model.addAttribute("idToken", idToken);
            return "profile";

        } catch (Exception e) {
            return "redirect:/login";
        }
    }

    private User createUserFromToken(FirebaseToken token) {
        User user = new User();
        user.setFirebaseUid(token.getUid());
        user.setUsername(token.getEmail().split("@")[0]);
        user.setEmail(token.getEmail());
        user.setFullName(token.getName());
        return user; // admin sẽ được set trong FirebaseAuthService
    }

    private void updateUserIfNeeded(User dbUser, FirebaseToken token) {
        boolean updated = false;
        if (!Objects.equals(dbUser.getEmail(), token.getEmail())) {
            dbUser.setEmail(token.getEmail());
            updated = true;
        }
        if (!Objects.equals(dbUser.getFullName(), token.getName())) {
            dbUser.setFullName(token.getName());
            updated = true;
        }
        if (!Objects.equals(dbUser.getUsername(), token.getEmail().split("@")[0])) {
            dbUser.setUsername(token.getEmail().split("@")[0]);
            updated = true;
        }
        if (updated) {
            dbUser.setUpdatedAt(java.time.LocalDateTime.now());
        }
    }

    private String extractTokenFromCookie(HttpServletRequest request) {
        if (request.getCookies() == null) return null;
        return Arrays.stream(request.getCookies())
                .filter(c -> "idToken".equals(c.getName()))
                .findFirst()
                .map(Cookie::getValue)
                .orElse(null);
    }
}