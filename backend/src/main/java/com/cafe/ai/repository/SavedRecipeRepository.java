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
     * Find saved recipes that used a particular surplus ingredient (partial match).
     */
    List<SavedRecipe> findBySurplusIngredientsContainingIgnoreCase(String ingredient);
}
