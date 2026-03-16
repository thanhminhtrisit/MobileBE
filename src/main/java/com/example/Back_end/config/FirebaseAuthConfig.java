package com.example.Back_end.config;

import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;

@Configuration
@DependsOn("firebaseApp")  // ÉP CHỜ firebaseApp BEAN HOÀN TẤT TRƯỚC
public class FirebaseAuthConfig {

    private final FirebaseApp firebaseApp;

    @Autowired
    public FirebaseAuthConfig(FirebaseApp firebaseApp) {
        this.firebaseApp = firebaseApp;
    }

    @Bean
    public FirebaseAuth firebaseAuth() {
        System.out.println("FirebaseAuth bean created successfully!");
        return FirebaseAuth.getInstance(firebaseApp);
    }
}
