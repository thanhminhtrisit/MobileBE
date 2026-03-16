package com.example.Back_end.config;

import com.example.Back_end.firebase.FirebaseAuthenticationFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@Configuration
public class SecurityConfig {

    private final FirebaseAuthenticationFilter firebaseFilter;

    public SecurityConfig(FirebaseAuthenticationFilter firebaseFilter) {
        this.firebaseFilter = firebaseFilter;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable()) // ✅ tắt CSRF
                .cors(cors -> {})             // ✅ bật CORS support
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/login", "/api/auth/**", "/users", "/api/users/**",
                                "/api/labs/**", "/api/rooms/**", "/api/room-slots/**","/api/requests/**","/api/notifications/**", // 👈 thêm dòng này
                                "/labs", "/dashboard", "/",
                                "/css/**", "/js/**", "/images/**", "/swagger-ui/**","/v3/api-docs/**")
                        .permitAll()
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form.disable())
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/login")
                        .invalidateHttpSession(true)
                        .deleteCookies("idToken")
                        .clearAuthentication(true)
                )
                .addFilterBefore(firebaseFilter, BasicAuthenticationFilter.class);

        return http.build();
    }

    // ✅ Thêm filter CORS để cho phép mọi domain/method
    @Bean
    public CorsFilter corsFilter() {
        CorsConfiguration config = new CorsConfiguration();
        config.addAllowedOriginPattern("*");
        config.addAllowedHeader("*");
        config.addAllowedMethod("*");
        config.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return new CorsFilter(source);
    }
}
