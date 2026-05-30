package com.cafe.ai.controller;

import java.time.Instant;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cafe.ai.dto.RecipeGenerationResponse;
import com.cafe.ai.dto.SaveRecipeRequest;
import com.cafe.ai.dto.SurplusIngredientRequest;
import com.cafe.ai.model.SavedRecipe;
import com.cafe.ai.service.GrokRecipeService;

/**
 * REST controller exposing endpoints for:
 *  - Generating AI recipe ideas from surplus ingredients (via Grok)
 *  - Saving a user's favorite recipe
 *  - Retrieving and deleting saved recipes
 */
@RestController
@RequestMapping("/api/recipes")
@CrossOrigin(origins = "http://localhost:5173") // Vite dev server default port
public class RecipeController {

    private final GrokRecipeService grokRecipeService;

    public RecipeController(GrokRecipeService grokRecipeService) {
        this.grokRecipeService = grokRecipeService;
    }

    /**
     * POST /api/recipes/generate
     * Accepts surplus ingredients and returns 3 AI-generated recipe ideas.
     */
    @PostMapping("/generate")
    public ResponseEntity<RecipeGenerationResponse> generateRecipes(
            @RequestBody SurplusIngredientRequest request) {

        if (request.getIngredients() == null || request.getIngredients().isBlank()) {
            return ResponseEntity.badRequest().build();
        }

        RecipeGenerationResponse response = grokRecipeService.generateRecipeIdeas(request.getIngredients());
        return ResponseEntity.ok(response);
    }

    /**
     * POST /api/recipes/save
     * Saves the user's selected recipe to the PostgreSQL database.
     */
    @PostMapping("/save")
    public ResponseEntity<SavedRecipe> saveRecipe(@RequestBody SaveRecipeRequest request) {
        SavedRecipe saved = grokRecipeService.saveRecipe(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    /**
     * GET /api/recipes/saved
     * Retrieves all saved recipes, ordered by most recent.
     */
    @GetMapping("/saved")
    public ResponseEntity<List<SavedRecipe>> getSavedRecipes() {
        List<SavedRecipe> recipes = grokRecipeService.getAllSavedRecipes();
        return ResponseEntity.ok(recipes);
    }

    /**
     * DELETE /api/recipes/saved/{id}
     * Deletes a saved recipe by ID.
     */
    @DeleteMapping("/saved/{id}")
    public ResponseEntity<Map<String, String>> deleteRecipe(@PathVariable Long id) {
        grokRecipeService.deleteRecipe(id);
        return ResponseEntity.ok(Map.of("message", "Recipe deleted successfully."));
    }

    /**
     * GET /api/recipes/health
     * Simple health endpoint used by internal scheduler.
     */
    @GetMapping("/health")
    public ResponseEntity<Map<String, String>> health() {
        return ResponseEntity.ok(Map.of(
                "status", "UP",
                "timestamp", Instant.now().toString()
        ));
    }
}
