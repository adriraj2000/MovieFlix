package com.example.MovieFlix.controller;

import com.example.MovieFlix.model.ApiResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * Health check and system status endpoints
 */
@RestController
@RequestMapping("/api/health")
public class HealthController {

    private static final Logger logger = LoggerFactory.getLogger(HealthController.class);

    @Value("${spring.application.name}")
    private String applicationName;

    @Value("${spring.profiles.active:default}")
    private String activeProfile;

    @GetMapping
    public ResponseEntity<ApiResponse<Map<String, Object>>> health() {
        logger.info("Health check requested");

        Map<String, Object> health = new HashMap<>();
        health.put("application", applicationName);
        health.put("status", "UP");
        health.put("profile", activeProfile);
        health.put("timestamp", LocalDateTime.now());
        health.put("version", "0.0.1-SNAPSHOT");

        return ResponseEntity.ok(ApiResponse.success("Application is running", health));
    }

    @GetMapping("/ping")
    public ResponseEntity<ApiResponse<String>> ping() {
        return ResponseEntity.ok(ApiResponse.success("pong"));
    }
}
