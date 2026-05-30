package com.cafe.ai.dto;

/**
 * Request DTO for saving a selected recipe to the database.
 */
public class SaveRecipeRequest {

    private String recipeName;
    private String description;
    private String ingredients;
    private String instructions;
    private String marketingDescription;
    private String surplusIngredients;
    private String estimatedPrepTime;
    private String difficulty;

    public SaveRecipeRequest() {}

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

    public String getIngredients() {
        return ingredients;
    }

    public void setIngredients(String ingredients) {
        this.ingredients = ingredients;
    }

    public String getInstructions() {
        return instructions;
    }

    public void setInstructions(String instructions) {
        this.instructions = instructions;
    }

    public String getMarketingDescription() {
        return marketingDescription;
    }

    public void setMarketingDescription(String marketingDescription) {
        this.marketingDescription = marketingDescription;
    }

    public String getSurplusIngredients() {
        return surplusIngredients;
    }

    public void setSurplusIngredients(String surplusIngredients) {
        this.surplusIngredients = surplusIngredients;
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
