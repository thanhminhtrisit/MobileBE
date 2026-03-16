// src/main/java/com/example/Back_end/firebase/FirebaseAuthenticationFilter.java
package com.example.Back_end.firebase;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseToken;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.*;

@Component
@RequiredArgsConstructor  // <-- Đảm bảo có @RequiredArgsConstructor
public class FirebaseAuthenticationFilter extends OncePerRequestFilter {

    // Phải là final + @RequiredArgsConstructor sẽ tự tạo constructor
    private final FirebaseAuth firebaseAuth;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String idToken = extractTokenFromCookie(request);

        if (idToken != null) {
            try {
                FirebaseToken decodedToken = firebaseAuth.verifyIdToken(idToken);
                String firebaseUid = decodedToken.getUid();

                Map<String, Object> attributes = Map.of(
                        "sub", firebaseUid,
                        "uid", firebaseUid
                );

                DefaultOAuth2User oAuth2User = new DefaultOAuth2User(
                        Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER")),
                        attributes,
                        "sub"
                );

                UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
                        oAuth2User, null, oAuth2User.getAuthorities()
                );
                auth.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(auth);

            } catch (FirebaseAuthException e) {
                clearCookie(response);
                SecurityContextHolder.clearContext();
            }
        }

        filterChain.doFilter(request, response);
    }

    private String extractTokenFromCookie(HttpServletRequest request) {
        if (request.getCookies() == null) return null;
        return Arrays.stream(request.getCookies())
                .filter(c -> "idToken".equals(c.getName()))
                .findFirst()
                .map(Cookie::getValue)
                .orElse(null);
    }

    private void clearCookie(HttpServletResponse response) {
        response.addHeader("Set-Cookie",
                "idToken=; Path=/; HttpOnly; Max-Age=0; SameSite=Lax");
    }
}