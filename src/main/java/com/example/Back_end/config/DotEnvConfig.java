package com.example.Back_end.config;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;

public class DotEnvConfig {
    public static void loadEnv() {
        try {
            // Ưu tiên đọc file .env ở thư mục gốc dự án (rất tiện)
            File rootEnv = new File(".env");
            if (rootEnv.exists()) {
                loadFromFile(rootEnv);
                System.out.println("[DotEnvConfig] ✅ Loaded .env from project root successfully");
                return;
            }

            // Fallback sang classpath (giống code bạn gửi)
            try (InputStream is = DotEnvConfig.class.getClassLoader().getResourceAsStream(".env")) {
                if (is != null) {
                    loadFromStream(is);
                    System.out.println("[DotEnvConfig] ✅ Loaded .env from classpath");
                    return;
                }
            }

            System.out.println("[DotEnvConfig] ⚠️ .env file not found, using application.properties only");
        } catch (Exception e) {
            System.err.println("[DotEnvConfig] ❌ Error loading .env: " + e.getMessage());
        }
    }

    private static void loadFromFile(File file) throws IOException {
        try (BufferedReader reader = Files.newBufferedReader(file.toPath())) {
            processLines(reader);
        }
    }

    private static void loadFromStream(InputStream is) throws IOException {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(is))) {
            processLines(reader);
        }
    }

    private static void processLines(BufferedReader reader) throws IOException {
        String line;
        while ((line = reader.readLine()) != null) {
            line = line.trim();
            if (line.isEmpty() || line.startsWith("#")) continue;
            int idx = line.indexOf('=');
            if (idx < 0) continue;
            String key = line.substring(0, idx).trim();
            String value = line.substring(idx + 1).trim();
            if (System.getenv(key) == null && System.getProperty(key) == null) {
                System.setProperty(key, value);
            }
        }
    }
}
