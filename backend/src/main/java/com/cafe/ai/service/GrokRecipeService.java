package com.cafe.ai.service;

import com.cafe.ai.config.GrokConfig;
import com.cafe.ai.dto.RecipeGenerationResponse;
import com.cafe.ai.dto.RecipeIdeaDto;
import com.cafe.ai.dto.SaveRecipeRequest;
import com.cafe.ai.model.SavedRecipe;
import com.cafe.ai.repository.SavedRecipeRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.*;

/**
 * Service class responsible for:
 *  1. Constructing the prompt and calling the Grok (xAI) API to generate creative recipe ideas.
 *  2. Parsing the structured JSON response from Grok into DTOs.
 *  3. Saving a user-selected recipe to PostgreSQL via JPA.
 *
 * Grok uses an OpenAI-compatible chat completions API:
 *   POST https://api.x.ai/v1/chat/completions
 *   Authorization: Bearer <API_KEY>
 */
@Service
public class GrokRecipeService {

    private static final Logger log = LoggerFactory.getLogger(GrokRecipeService.class);

    private final RestTemplate restTemplate;
    private final GrokConfig grokConfig;
    private final SavedRecipeRepository savedRecipeRepository;
    private final ObjectMapper objectMapper;

    public GrokRecipeService(RestTemplate restTemplate,
                              GrokConfig grokConfig,
                              SavedRecipeRepository savedRecipeRepository,
                              ObjectMapper objectMapper) {
        this.restTemplate = restTemplate;
        this.grokConfig = grokConfig;
        this.savedRecipeRepository = savedRecipeRepository;
        this.objectMapper = objectMapper;
    }

    // =========================================================================
    //  1. GENERATE RECIPES VIA GROK API
    // =========================================================================

    /**
     * Calls the Grok API with a carefully crafted prompt to generate
     * 3 creative recipe ideas from surplus cafe ingredients.
     *
     * @param surplusIngredients comma-separated list, e.g. "excess milk, vanilla extract, stale bread"
     * @return a RecipeGenerationResponse containing 3 recipe ideas
     */
    public RecipeGenerationResponse generateRecipeIdeas(String surplusIngredients) {
        log.info("Generating recipe ideas for surplus ingredients: {}", surplusIngredients);

        String apiKey = grokConfig.getApiKey();
        if (apiKey == null || apiKey.isBlank()) {
            throw new IllegalStateException(
                    "Grok API key is not configured. Set GROK_API_KEY or update grok.api.key in application.properties."
            );
        }

        // Build the prompt
        String prompt = buildPrompt(surplusIngredients);

        // Build the Grok API request body (OpenAI-compatible format)
        Map<String, Object> requestBody = buildGrokRequestBody(prompt);

        // Set headers — Grok uses Bearer token authentication
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(apiKey);

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);

        // Call the Grok API
        ResponseEntity<String> response;
        try {
            response = restTemplate.exchange(
                    grokConfig.getApiUrl(),
                    HttpMethod.POST,
                    entity,
                    String.class
            );
        } catch (HttpClientErrorException ex) {
            String body = ex.getResponseBodyAsString();
            log.error("Grok API request failed with status {}: {}", ex.getStatusCode(), body);
            if (ex.getStatusCode() == HttpStatus.BAD_REQUEST && body != null && body.contains("Incorrect API key")) {
                throw new IllegalStateException(
                        "The Grok API key is invalid. Set GROK_API_KEY to a valid xAI API key.",
                        ex
                );
            }
            throw ex;
        }

        if (!response.getStatusCode().is2xxSuccessful() || response.getBody() == null) {
            log.error("Grok API returned status: {}", response.getStatusCode());
            throw new RuntimeException("Failed to get response from Grok API. Status: " + response.getStatusCode());
        }

        // Parse the response into recipe DTOs
        List<RecipeIdeaDto> recipes = parseGrokResponse(response.getBody());

        return new RecipeGenerationResponse(recipes, surplusIngredients);
    }

    /**
     * Builds a structured prompt that instructs Grok to return valid JSON
     * with exactly 3 creative recipe ideas for a cafe menu.
     */
    private String buildPrompt(String surplusIngredients) {
        return """
            You are a world-class cafe chef and creative menu consultant.
            A cafe has the following surplus ingredients that need to be used before they expire:
            
            **Surplus Ingredients:** %s
            
            Generate exactly 3 creative, unique, and delicious recipe ideas that:
            1. Primarily use the surplus ingredients listed above.
            2. May include common pantry staples (sugar, flour, butter, eggs, salt, etc.) as supplementary ingredients.
            3. Are suitable for a trendy cafe menu.
            4. Range in difficulty from easy to moderate.
            5. Have catchy, marketable names.
            
            For each recipe, also write a compelling marketing description (2-3 sentences) that would
            entice customers to order it from a cafe menu board or social media post.
            
            **IMPORTANT:** Respond ONLY with a valid JSON array. No markdown, no code fences, no explanation.
            Use this exact structure:
            
            [
              {
                "recipeName": "Creative Recipe Name",
                "description": "Brief 1-2 sentence description of the dish.",
                "ingredients": ["ingredient 1 with quantity", "ingredient 2 with quantity"],
                "instructions": ["Step 1 instruction", "Step 2 instruction"],
                "marketingDescription": "An enticing 2-3 sentence marketing blurb for the cafe menu.",
                "estimatedPrepTime": "e.g., 25 minutes",
                "difficulty": "Easy | Medium | Hard"
              }
            ]
            """.formatted(surplusIngredients);
    }

    /**
     * Builds the JSON request body for the Grok API (OpenAI-compatible chat completions format).
     *
     * Structure:
     * {
     *   "model": "grok-3",
     *   "messages": [
     *     { "role": "system", "content": "You are a helpful assistant that responds in JSON." },
     *     { "role": "user", "content": "<prompt>" }
     *   ],
     *   "temperature": 0.9,
     *   "max_tokens": 4096
     * }
     */
    private Map<String, Object> buildGrokRequestBody(String prompt) {
        // System message to reinforce JSON-only output
        Map<String, String> systemMessage = new HashMap<>();
        systemMessage.put("role", "system");
        systemMessage.put("content",
                "You are a creative cafe chef assistant. You MUST respond with ONLY a valid JSON array. " +
                "No markdown formatting, no code fences, no explanatory text — just raw JSON.");

        // User message with the actual prompt
        Map<String, String> userMessage = new HashMap<>();
        userMessage.put("role", "user");
        userMessage.put("content", prompt);

        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("model", grokConfig.getModel());
        requestBody.put("messages", List.of(systemMessage, userMessage));
        requestBody.put("temperature", 0.9);        // Higher creativity
        requestBody.put("max_tokens", 4096);

        return requestBody;
    }

    /**
     * Parses the raw Grok API JSON response and extracts the recipe list.
     * Handles the OpenAI-compatible response structure:
     *   { "choices": [{ "message": { "content": "..." } }] }
     */
    private List<RecipeIdeaDto> parseGrokResponse(String responseBody) {
        try {
            JsonNode root = objectMapper.readTree(responseBody);

            // Navigate: choices[0].message.content
            JsonNode choicesNode = root.path("choices");
            if (choicesNode.isMissingNode() || !choicesNode.isArray() || choicesNode.isEmpty()) {
                throw new RuntimeException("No choices found in Grok response.");
            }

            String generatedText = choicesNode.get(0)
                    .path("message")
                    .path("content")
                    .asText();

            // Clean the response — strip markdown code fences if Grok wraps them
            generatedText = generatedText.trim();
            if (generatedText.startsWith("```json")) {
                generatedText = generatedText.substring(7);
            } else if (generatedText.startsWith("```")) {
                generatedText = generatedText.substring(3);
            }
            if (generatedText.endsWith("```")) {
                generatedText = generatedText.substring(0, generatedText.length() - 3);
            }
            generatedText = generatedText.trim();

            // Deserialize the JSON array into a list of DTOs
            List<RecipeIdeaDto> recipes = objectMapper.readValue(
                    generatedText,
                    new TypeReference<List<RecipeIdeaDto>>() {}
            );

            log.info("Successfully parsed {} recipes from Grok response.", recipes.size());
            return recipes;

        } catch (Exception e) {
            log.error("Failed to parse Grok response: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to parse recipe ideas from AI response.", e);
        }
    }

    // =========================================================================
    //  2. SAVE A SELECTED RECIPE TO POSTGRESQL
    // =========================================================================

    /**
     * Saves the user's selected recipe idea to the PostgreSQL database.
     *
     * @param request the recipe data to persist
     * @return the persisted SavedRecipe entity with its generated ID
     */
    public SavedRecipe saveRecipe(SaveRecipeRequest request) {
        log.info("Saving recipe: {}", request.getRecipeName());

        SavedRecipe entity = new SavedRecipe();
        entity.setRecipeName(request.getRecipeName());
        entity.setDescription(request.getDescription());
        entity.setIngredients(request.getIngredients());
        entity.setInstructions(request.getInstructions());
        entity.setMarketingDescription(request.getMarketingDescription());
        entity.setSurplusIngredients(request.getSurplusIngredients());
        entity.setEstimatedPrepTime(request.getEstimatedPrepTime());
        entity.setDifficulty(request.getDifficulty());

        SavedRecipe saved = savedRecipeRepository.save(entity);
        log.info("Recipe saved with ID: {}", saved.getId());

        return saved;
    }

    // =========================================================================
    //  3. RETRIEVE SAVED RECIPES
    // =========================================================================

    /**
     * Retrieves all saved recipes, ordered by most recent first.
     */
    public List<SavedRecipe> getAllSavedRecipes() {
        return savedRecipeRepository.findAllByOrderBySavedAtDesc();
    }

    /**
     * Deletes a saved recipe by its ID.
     */
    public void deleteRecipe(Long id) {
        if (!savedRecipeRepository.existsById(id)) {
            throw new RuntimeException("Recipe not found with ID: " + id);
        }
        savedRecipeRepository.deleteById(id);
        log.info("Deleted recipe with ID: {}", id);
    }
}
