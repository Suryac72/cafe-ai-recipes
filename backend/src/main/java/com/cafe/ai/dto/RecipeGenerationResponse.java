package com.cafe.ai.dto;

import java.util.List;

/**
 * Wrapper response DTO containing all generated recipe ideas.
 */
public class RecipeGenerationResponse {

    private List<RecipeIdeaDto> recipes;
    private String surplusIngredients;

    public RecipeGenerationResponse() {}

    public RecipeGenerationResponse(List<RecipeIdeaDto> recipes, String surplusIngredients) {
        this.recipes = recipes;
        this.surplusIngredients = surplusIngredients;
    }

    public List<RecipeIdeaDto> getRecipes() {
        return recipes;
    }

    public void setRecipes(List<RecipeIdeaDto> recipes) {
        this.recipes = recipes;
    }

    public String getSurplusIngredients() {
        return surplusIngredients;
    }

    public void setSurplusIngredients(String surplusIngredients) {
        this.surplusIngredients = surplusIngredients;
    }
}
