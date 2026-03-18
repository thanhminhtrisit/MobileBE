package com.example.Back_end;

import com.example.Back_end.config.DotEnvConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SwdBackEndApplication {

	public static void main(String[] args) {
		DotEnvConfig.loadEnv();                    // ← Thêm dòng này (quan trọng nhất)
		SpringApplication.run(SwdBackEndApplication.class, args);
        System.out.println("Hello World!");
	}

}
