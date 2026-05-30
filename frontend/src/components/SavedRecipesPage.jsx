import React, { useState, useEffect } from 'react';
import { getSavedRecipes, deleteRecipe } from '../services/recipeApi';
import './SavedRecipesPage.css';

export default function SavedRecipesPage() {
  const [recipes, setRecipes] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const [deletingId, setDeletingId] = useState(null);
  const [toast, setToast] = useState(null);

  const showToast = (message, type = 'success') => {
    setToast({ message, type });
    setTimeout(() => setToast(null), 3000);
  };

  useEffect(() => {
    loadRecipes();
  }, []);

  const loadRecipes = async () => {
    try {
      setLoading(true);
      const data = await getSavedRecipes();
      setRecipes(data);
    } catch (err) {
      setError('Failed to load saved recipes.');
    } finally {
      setLoading(false);
    }
  };

  const handleDelete = async (id, name) => {
    if (!window.confirm(`Remove "${name}" from saved recipes?`)) return;

    setDeletingId(id);
    try {
      await deleteRecipe(id);
      setRecipes((prev) => prev.filter((r) => r.id !== id));
      showToast(`"${name}" removed successfully.`);
    } catch (err) {
      showToast('Failed to delete recipe.', 'error');
    } finally {
      setDeletingId(null);
    }
  };

  const getDifficultyClass = (difficulty) => {
    if (!difficulty) return '';
    return `saved-card__tag--${difficulty}`;
  };

  if (loading) {
    return (
      <div className="saved-page">
        <div className="saved-page__loading">
          <div className="saved-page__spinner" />
          <p>Loading saved recipes…</p>
        </div>
      </div>
    );
  }

  return (
    <div className="saved-page">
      <header className="saved-page__header">
        <h1 className="saved-page__title">Saved Recipes</h1>
        <p className="saved-page__subtitle">
          Your curated collection of AI-generated menu items
        </p>
        <div className="saved-page__count">
          {recipes.length} recipe{recipes.length !== 1 ? 's' : ''} saved
        </div>
      </header>

      {error && (
        <div className="saved-page__error">
          ⚠️ {error}
        </div>
      )}

      {recipes.length === 0 && !error ? (
        <div className="saved-page__empty">
          <div className="saved-page__empty-icon">📋</div>
          <h2>No recipes saved yet</h2>
          <p>Generate recipes from surplus ingredients and save your favorites here.</p>
        </div>
      ) : (
        <div className="saved-cards-grid" id="saved-recipes-grid">
          {recipes.map((recipe) => (
            <article className="saved-card" key={recipe.id} id={`saved-recipe-${recipe.id}`}>
              <div className="saved-card__accent" />
              <div className="saved-card__body">
                <div className="saved-card__meta">
                  <span className="saved-card__tag saved-card__tag--time">
                    ⏱ {recipe.estimatedPrepTime}
                  </span>
                  <span className={`saved-card__tag ${getDifficultyClass(recipe.difficulty)}`}>
                    {recipe.difficulty}
                  </span>
                </div>

                <h3 className="saved-card__name">{recipe.recipeName}</h3>
                <p className="saved-card__description">{recipe.description}</p>

                <div className="saved-card__marketing">
                  <div className="saved-card__marketing-label">✨ Menu Description</div>
                  <p className="saved-card__marketing-text">
                    "{recipe.marketingDescription}"
                  </p>
                </div>

                <div className="saved-card__ingredients-preview">
                  <span className="saved-card__section-label">Surplus Used:</span>
                  <span className="saved-card__surplus">{recipe.surplusIngredients}</span>
                </div>

                <div className="saved-card__date">
                  Saved {new Date(recipe.savedAt).toLocaleDateString('en-US', {
                    month: 'short',
                    day: 'numeric',
                    year: 'numeric',
                  })}
                </div>
              </div>

              <div className="saved-card__footer">
                <button
                  className="saved-card__delete-btn"
                  onClick={() => handleDelete(recipe.id, recipe.recipeName)}
                  disabled={deletingId === recipe.id}
                  id={`delete-recipe-${recipe.id}`}
                >
                  {deletingId === recipe.id ? 'Removing…' : '🗑 Remove'}
                </button>
              </div>
            </article>
          ))}
        </div>
      )}

      {toast && (
        <div className="saved-page__toast-container">
          <div className={`saved-page__toast saved-page__toast--${toast.type}`}>
            {toast.type === 'success' ? '✓' : '✗'} {toast.message}
          </div>
        </div>
      )}
    </div>
  );
}
