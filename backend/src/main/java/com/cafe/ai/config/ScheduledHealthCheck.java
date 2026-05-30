package com.cafe.ai.config;

import java.time.Instant;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class ScheduledHealthCheck {

    private static final Logger log = LoggerFactory.getLogger(ScheduledHealthCheck.class);

    // Runs every 15 minutes (at minute 0,15,30,45)
    @Scheduled(cron = "0 */15 * * * *")
    public ResponseEntity<Map<String, Object>> triggerHealthCheck() {
        Map<String, Object> response = Map.of(
                "status", "UP",
                "service", "cafe-ai-recipes",
                "timestamp", Instant.now().toString()
        );
        LoggerFactory.getLogger(ScheduledHealthCheck.class).info("Scheduled health check triggered: {}", response);
        return ResponseEntity.ok(response);
    }
}
