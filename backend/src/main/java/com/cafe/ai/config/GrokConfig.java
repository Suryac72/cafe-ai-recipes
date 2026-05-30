package com.cafe.ai.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

/**
 * Configuration class for Grok (xAI) API integration.
 * Grok uses an OpenAI-compatible REST API with Bearer token authentication.
 */
@Configuration
public class GrokConfig {

    @Value("${grok.api.key}")
    private String apiKey;

    @Value("${grok.api.url:https://api.x.ai/v1/chat/completions}")
    private String apiUrl;

    @Value("${grok.api.model:llama-3.1-8b-instant}")
    private String model;

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    public String getApiKey() {
        return apiKey;
    }

    public String getApiUrl() {
        return apiUrl;
    }

    public String getModel() {
        return model;
    }
}
