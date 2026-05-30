package com.cafe.ai.controller;

import java.time.Instant;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Root health endpoint for external monitoring and health checks.
 */
@RestController
class RootHealthController {
    
    @GetMapping("/")
    public ResponseEntity<Map<String, String>> rootHealth() {
        return ResponseEntity.ok(Map.of(
                "status", "UP",
                "service", "cafe-ai-recipes",
                "timestamp", Instant.now().toString()
        ));
    }
}