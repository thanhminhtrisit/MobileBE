// src/main/java/com/example/Back_end/firebase/FirebaseAuthService.java
package com.example.Back_end.firebase;

import com.example.Back_end.dto.GoogleSignInRequest;
import com.example.Back_end.dto.UserResponseDTO;
import com.example.Back_end.entity.User;
import com.example.Back_end.repository.UserRepository;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseToken;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Transactional
public class FirebaseAuthService {

    private final FirebaseAuth firebaseAuth;
    private final UserRepository userRepository;

    private static final String ADMIN_EMAIL = "hailqse183698@fpt.edu.vn";

    public UserResponseDTO signInWithGoogle(GoogleSignInRequest request) {
        try {
            FirebaseToken decodedToken = firebaseAuth.verifyIdToken(request.idToken());
            String firebaseUid = decodedToken.getUid();
            String email = decodedToken.getEmail();

            // 🔹 1️⃣ Ưu tiên tìm theo Firebase UID
            User user = userRepository.findByFirebaseUid(firebaseUid)
                    // 🔹 2️⃣ Nếu chưa có UID này, thử tìm theo email (admin tạo trước)
                    .orElseGet(() -> userRepository.findByEmail(email)
                            .orElseGet(() -> createUserFromToken(decodedToken)));

            boolean updated = false;

            // 🔹 3️⃣ Nếu user chưa có UID thật (vì admin tạo thủ công) → cập nhật lại
            if (user.getFirebaseUid() == null || user.getFirebaseUid().startsWith("temp-")) {
                user.setFirebaseUid(firebaseUid);
                updated = true;
            }

            // 🔹 4️⃣ Cập nhật email hoặc fullname nếu khác
            if (email != null && !email.equals(user.getEmail())) {
                user.setEmail(email);
                updated = true;
            }

            String name = decodedToken.getName();
            if (name != null && (user.getFullName() == null || !name.equals(user.getFullName()))) {
                user.setFullName(name);
                updated = true;
            }

            // 🔹 5️⃣ Chỉ save nếu có thay đổi
            if (updated) {
                user = userRepository.save(user);
                System.out.println("🔄 Updated user info for: " + user.getUsername());
            }

            return mapToUserResponse(user);

        } catch (FirebaseAuthException e) {
            throw new RuntimeException("Invalid Google token: " + e.getMessage());
        }
    }



    private User createUserFromToken(FirebaseToken token) {
        User user = new User();
        user.setFirebaseUid(token.getUid());
        user.setUsername(token.getEmail().split("@")[0]);
        user.setEmail(token.getEmail());
        user.setFullName(token.getName());

        // TỰ ĐỘNG CẤP QUYỀN ADMIN
        user.setAdmin(ADMIN_EMAIL.equals(token.getEmail()));

        User savedUser = userRepository.save(user);
        System.out.println("NEW USER CREATED - UID: " + token.getUid() +
                " | DB ID: " + savedUser.getUserId() +
                " | ADMIN: " + savedUser.isAdmin());
        return savedUser;
    }

    private UserResponseDTO mapToUserResponse(User user) {
        UserResponseDTO dto = new UserResponseDTO();
        dto.setUserId(user.getUserId());
        dto.setUsername(user.getUsername());
        dto.setEmail(user.getEmail());
        dto.setFullName(user.getFullName());
        dto.setPhone(user.getPhone());
        dto.setFirebaseUid(user.getFirebaseUid());
        dto.setAdmin(user.isAdmin());
        dto.setCreatedAt(user.getCreatedAt());
        dto.setUpdatedAt(user.getUpdatedAt());
        return dto;
    }
}