import React, { useState, useCallback } from 'react';
import RecipeCard from './RecipeCard';
import { generateRecipes, saveRecipe } from '../services/recipeApi';
import './SurplusRecipeGenerator.css';

/**
 * SurplusRecipeGenerator — Main component for the AI Recipe feature.
 *
 * Flow:
 *  1. User enters surplus ingredients in the textarea.
 *  2. Clicks "Generate Ideas" → calls Spring Boot → Gemini API.
 *  3. 3 recipe cards appear with staggered animations.
 *  4. User clicks "Save to Menu" on their favorite → persists to PostgreSQL.
 */
export default function SurplusRecipeGenerator() {
  // --- State ---
  const [ingredients, setIngredients] = useState('');
  const [recipes, setRecipes] = useState([]);
  const [surplusIngredients, setSurplusIngredients] = useState('');
  const [isLoading, setIsLoading] = useState(false);
  const [error, setError] = useState(null);
  const [savingIndex, setSavingIndex] = useState(null);
  const [savedIndices, setSavedIndices] = useState(new Set());
  const [toast, setToast] = useState(null);

  // --- Toast helper ---
  const showToast = useCallback((message, type = 'success') => {
    setToast({ message, type });
    setTimeout(() => setToast(null), 3500);
  }, []);

  // --- Generate recipes ---
  const handleGenerate = async () => {
    if (!ingredients.trim()) return;

    setIsLoading(true);
    setError(null);
    setRecipes([]);
    setSavedIndices(new Set());

    try {
      const response = await generateRecipes(ingredients.trim());
      setRecipes(response.recipes);
      setSurplusIngredients(response.surplusIngredients);
    } catch (err) {
      setError(err.message || 'Something went wrong. Please try again.');
    } finally {
      setIsLoading(false);
    }
  };

  // --- Save a recipe ---
  const handleSave = async (recipeData, index) => {
    setSavingIndex(index);

    try {
      await saveRecipe(recipeData);
      setSavedIndices((prev) => new Set(prev).add(index));
      showToast(`"${recipeData.recipeName}" saved to your menu!`, 'success');
    } catch (err) {
      showToast('Failed to save recipe. Please try again.', 'error');
    } finally {
      setSavingIndex(null);
    }
  };

  // --- Keyboard shortcut: Ctrl+Enter to generate ---
  const handleKeyDown = (e) => {
    if ((e.ctrlKey || e.metaKey) && e.key === 'Enter') {
      handleGenerate();
    }
  };

  return (
    <div className="recipe-generator">
      {/* ---- Header ---- */}
      <header className="recipe-generator__header">
        <div className="recipe-generator__badge">
          <svg
            xmlns="http://www.w3.org/2000/svg"
            viewBox="0 0 24 24"
            fill="none"
            stroke="currentColor"
            strokeWidth="2"
            strokeLinecap="round"
            strokeLinejoin="round"
          >
            <path d="M12 2L2 7l10 5 10-5-10-5z" />
            <path d="M2 17l10 5 10-5" />
            <path d="M2 12l10 5 10-5" />
          </svg>
          AI-Powered
        </div>
        <h1 className="recipe-generator__title">
          Surplus Ingredient
          <br />
          Recipe Generator
        </h1>
        <p className="recipe-generator__subtitle">
          Turn your cafe's surplus ingredients into creative, profitable menu items.
          Powered by Google Gemini AI.
        </p>
      </header>

      {/* ---- Input Section ---- */}
      <section className="recipe-generator__input-section" id="ingredient-input-section">
        <div className="input-card">
          <label className="input-card__label" htmlFor="surplus-ingredients">
            Surplus Ingredients
          </label>
          <textarea
            id="surplus-ingredients"
            className="input-card__textarea"
            placeholder="e.g., excess milk, vanilla extract, stale bread, overripe bananas, leftover cream cheese..."
            value={ingredients}
            onChange={(e) => setIngredients(e.target.value)}
            onKeyDown={handleKeyDown}
            disabled={isLoading}
          />
          <div className="input-card__hint">
            💡 Separate ingredients with commas. Press Ctrl+Enter to generate.
          </div>
          <div className="input-card__actions">
            <button
              className="btn btn--primary"
              onClick={handleGenerate}
              disabled={isLoading || !ingredients.trim()}
              id="generate-btn"
            >
              {isLoading ? (
                <>
                  <span
                    className="loading-spinner"
                    style={{ width: 18, height: 18, borderWidth: 2 }}
                  />
                  Generating…
                </>
              ) : (
                <>🧑‍🍳 Generate Recipe Ideas</>
              )}
            </button>
          </div>
        </div>
      </section>

      {/* ---- Error Banner ---- */}
      {error && (
        <div className="error-banner" role="alert">
          <span className="error-banner__icon">⚠️</span>
          <span className="error-banner__text">{error}</span>
        </div>
      )}

      {/* ---- Loading State ---- */}
      {isLoading && (
        <div className="loading-section">
          <div className="loading-spinner" />
          <p className="loading-text">
            Gemini AI is crafting creative recipes…
          </p>
          <p className="loading-subtext">
            This usually takes 5–10 seconds.
          </p>
        </div>
      )}

      {/* ---- Recipe Cards ---- */}
      {recipes.length > 0 && !isLoading && (
        <>
          <div className="recipe-cards-header">
            <h2>Your AI-Generated Recipes</h2>
            <p>Click on a recipe to expand details, then save your favorite to the menu.</p>
          </div>

          <div className="recipe-cards-grid" id="recipe-cards-grid">
            {recipes.map((recipe, index) => (
              <RecipeCard
                key={index}
                recipe={recipe}
                index={index}
                onSave={(data) => handleSave(data, index)}
                isSaving={savingIndex === index}
                isSaved={savedIndices.has(index)}
                surplusIngredients={surplusIngredients}
              />
            ))}
          </div>
        </>
      )}

      {/* ---- Toast Notification ---- */}
      {toast && (
        <div className="toast-container">
          <div className={`toast toast--${toast.type}`}>
            {toast.type === 'success' ? '✓' : '✗'} {toast.message}
          </div>
        </div>
      )}
    </div>
  );
}
