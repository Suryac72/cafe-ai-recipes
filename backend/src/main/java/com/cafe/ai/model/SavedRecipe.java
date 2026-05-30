package com.cafe.ai.model;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

/**
 * JPA Entity representing a recipe idea saved by the user.
 * Stores the AI-generated recipe along with the original surplus ingredients.
 */
@Entity
@Table(name = "saved_recipes")
public class SavedRecipe {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String recipeName;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String description;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String ingredients;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String instructions;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String marketingDescription;

    @Column(nullable = false)
    private String surplusIngredients;

    @Column(nullable = false)
    private String estimatedPrepTime;

    @Column(nullable = false)
    private String difficulty;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime savedAt;

    // --- Constructors ---

    public SavedRecipe() {}

    // --- Getters and Setters ---

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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

    public LocalDateTime getSavedAt() {
        return savedAt;
    }

    public void setSavedAt(LocalDateTime savedAt) {
        this.savedAt = savedAt;
    }
}
