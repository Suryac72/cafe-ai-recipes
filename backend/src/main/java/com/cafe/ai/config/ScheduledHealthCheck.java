package com.cafe.ai.config;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

@Component
public class ScheduledHealthCheck {

    private static final Logger log = LoggerFactory.getLogger(ScheduledHealthCheck.class);

    private final RestTemplate restTemplate;

    @Value("${health.check.url:http://localhost:8080/api/recipes/health}")
    private String healthUrl;

    public ScheduledHealthCheck(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    // Runs every 15 minutes (at minute 0,15,30,45)
    @Scheduled(cron = "0 */15 * * * *")
    public void triggerHealthCheck() {
        try {
            ResponseEntity<Map> resp = restTemplate.getForEntity(healthUrl, Map.class);
            log.info("Health check to {} returned: {}", healthUrl, resp.getStatusCode());
        } catch (RestClientException ex) {
            log.warn("Health check to {} failed: {}", healthUrl, ex.getMessage());
        }
    }
}
