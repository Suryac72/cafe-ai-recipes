package com.cafe.ai.repository;

import com.cafe.ai.model.SavedRecipe;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Spring Data JPA repository for the SavedRecipe entity.
 */
@Repository
public interface SavedRecipeRepository extends JpaRepository<SavedRecipe, Long> {

    /**
     * Find all saved recipes ordered by most recently saved first.
     */
    List<SavedRecipe> findAllByOrderBySavedAtDesc();

    /**
     * Find saved recipes for a specific user ordered by most recently saved first.
     */
    List<SavedRecipe> findAllByUserIdOrderBySavedAtDesc(String userId);

    /**
     * Find a saved recipe by ID that belongs to a specific user.
     */
    java.util.Optional<SavedRecipe> findByIdAndUserId(Long id, String userId);

    /**
     * Find saved recipes that used a particular surplus ingredient (partial match).
     */
    List<SavedRecipe> findBySurplusIngredientsContainingIgnoreCase(String ingredient);
}
