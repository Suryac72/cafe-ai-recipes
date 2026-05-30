package com.cafe.ai.dto;

import java.util.List;

/**
 * Request DTO for surplus ingredient input.
 */
public class SurplusIngredientRequest {

    private String ingredients;

    public SurplusIngredientRequest() {}

    public SurplusIngredientRequest(String ingredients) {
        this.ingredients = ingredients;
    }

    public String getIngredients() {
        return ingredients;
    }

    public void setIngredients(String ingredients) {
        this.ingredients = ingredients;
    }
}
