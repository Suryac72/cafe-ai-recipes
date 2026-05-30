package com.cafe.ai.dto;

import java.util.List;

/**
 * DTO representing a single AI-generated recipe idea.
 */
public class RecipeIdeaDto {

    private String recipeName;
    private String description;
    private List<String> ingredients;
    private List<String> instructions;
    private String marketingDescription;
    private String estimatedPrepTime;
    private String difficulty;

    public RecipeIdeaDto() {}

    // --- Getters and Setters ---

    public String getRecipeName() {
        return recipeName;
    }

    public void setRecipeName(String recipeName) {
        this.recipeName = recipeName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<String> getIngredients() {
        return ingredients;
    }

    public void setIngredients(List<String> ingredients) {
        this.ingredients = ingredients;
    }

    public List<String> getInstructions() {
        return instructions;
    }

    public void setInstructions(List<String> instructions) {
        this.instructions = instructions;
    }

    public String getMarketingDescription() {
        return marketingDescription;
    }

    public void setMarketingDescription(String marketingDescription) {
        this.marketingDescription = marketingDescription;
    }

    public String getEstimatedPrepTime() {
        return estimatedPrepTime;
    }

    public void setEstimatedPrepTime(String estimatedPrepTime) {
        this.estimatedPrepTime = estimatedPrepTime;
    }

    public String getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(String difficulty) {
        this.difficulty = difficulty;
    }
}
