package com.pranikov.portfolio.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig implements WebMvcConfigurer {

    // Optional CSV of allowed origins (can be empty)
    @Value("${app.cors.allowed-origins:}")
    private String allowedOrigins;

    // Default allowed origin patterns (local + deployed frontend)
    @Value("${app.cors.allowed-origin-patterns:http://localhost:*,http://127.0.0.1:*,https://pranikov-dev.vercel.app}")
    private String allowedOriginPatterns;

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        var reg = registry
                .addMapping("/api/**") // apply to all /api endpoints
                .allowedMethods("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS")
                .allowedHeaders("*")
                .allowCredentials(true); // <-- changed from false to true

        String[] patterns = splitCsv(allowedOriginPatterns);
        if (patterns.length > 0) {
            reg.allowedOriginPatterns(patterns);
        } else {
            reg.allowedOrigins(splitCsv(allowedOrigins));
        }
    }

    // Utility to split comma-separated strings into array
    private String[] splitCsv(String csv) {
        return csv == null || csv.isBlank()
                ? new String[0]
                : csv.split("\\s*,\\s*");
    }
}
