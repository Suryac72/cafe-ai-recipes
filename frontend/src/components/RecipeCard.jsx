import React, { useState } from 'react';

/**
 * RecipeCard — A single AI-generated recipe idea displayed as a premium card.
 * Features expandable ingredient/instruction sections and a save button.
 *
 * @param {Object}   recipe           - The recipe data (RecipeIdeaDto)
 * @param {number}   index            - Card index (0–2) for staggered animations and accent colors
 * @param {Function} onSave           - Callback when save button is clicked
 * @param {boolean}  isSaving         - Whether a save operation is in progress
 * @param {boolean}  isSaved          - Whether this recipe has already been saved
 * @param {string}   surplusIngredients - Original surplus ingredients string
 */
export default function RecipeCard({
  recipe,
  index,
  onSave,
  isSaving,
  isSaved,
  surplusIngredients,
}) {
  const [showIngredients, setShowIngredients] = useState(false);
  const [showInstructions, setShowInstructions] = useState(false);

  const accentClass = `recipe-card__accent--${index + 1}`;

  const handleSave = () => {
    onSave({
      recipeName: recipe.recipeName,
      description: recipe.description,
      ingredients: recipe.ingredients.join('\n'),
      instructions: recipe.instructions.join('\n'),
      marketingDescription: recipe.marketingDescription,
      surplusIngredients,
      estimatedPrepTime: recipe.estimatedPrepTime,
      difficulty: recipe.difficulty,
    });
  };

  /* Chevron SVG icon */
  const ChevronIcon = () => (
    <svg
      xmlns="http://www.w3.org/2000/svg"
      viewBox="0 0 24 24"
      fill="none"
      stroke="currentColor"
      strokeWidth="2"
      strokeLinecap="round"
      strokeLinejoin="round"
    >
      <polyline points="6 9 12 15 18 9" />
    </svg>
  );

  return (
    <article className="recipe-card" id={`recipe-card-${index}`}>
      {/* Color accent bar */}
      <div className={`recipe-card__accent ${accentClass}`} />

      <div className="recipe-card__body">
        {/* Meta badges */}
        <div className="recipe-card__meta">
          <span className="recipe-card__tag recipe-card__tag--time">
            ⏱ {recipe.estimatedPrepTime}
          </span>
          <span
            className={`recipe-card__tag recipe-card__tag--difficulty recipe-card__tag--${recipe.difficulty}`}
          >
            {recipe.difficulty}
          </span>
        </div>

        {/* Title & description */}
        <h3 className="recipe-card__name">{recipe.recipeName}</h3>
        <p className="recipe-card__description">{recipe.description}</p>

        {/* Marketing blurb */}
        <div className="recipe-card__marketing">
          <div className="recipe-card__marketing-label">✨ Menu Description</div>
          <p className="recipe-card__marketing-text">
            "{recipe.marketingDescription}"
          </p>
        </div>

        {/* Expandable: Ingredients */}
        <div className="recipe-card__section">
          <button
            className={`recipe-card__section-toggle ${
              showIngredients ? 'recipe-card__section-toggle--open' : ''
            }`}
            onClick={() => setShowIngredients(!showIngredients)}
            aria-expanded={showIngredients}
            id={`ingredients-toggle-${index}`}
          >
            Ingredients ({recipe.ingredients.length})
            <ChevronIcon />
          </button>
          <div
            className={`recipe-card__section-content ${
              showIngredients ? 'recipe-card__section-content--open' : ''
            }`}
          >
            <ul className="recipe-card__ingredients-list">
              {recipe.ingredients.map((item, i) => (
                <li key={i}>{item}</li>
              ))}
            </ul>
          </div>
        </div>

        {/* Expandable: Instructions */}
        <div className="recipe-card__section">
          <button
            className={`recipe-card__section-toggle ${
              showInstructions ? 'recipe-card__section-toggle--open' : ''
            }`}
            onClick={() => setShowInstructions(!showInstructions)}
            aria-expanded={showInstructions}
            id={`instructions-toggle-${index}`}
          >
            Instructions ({recipe.instructions.length} steps)
            <ChevronIcon />
          </button>
          <div
            className={`recipe-card__section-content ${
              showInstructions ? 'recipe-card__section-content--open' : ''
            }`}
          >
            <ol className="recipe-card__instructions-list">
              {recipe.instructions.map((step, i) => (
                <li key={i}>{step}</li>
              ))}
            </ol>
          </div>
        </div>
      </div>

      {/* Footer with save button */}
      <div className="recipe-card__footer">
        {isSaved ? (
          <button className="btn btn--saved" disabled id={`save-btn-${index}`}>
            ✓ Saved to Menu
          </button>
        ) : (
          <button
            className="btn btn--save"
            onClick={handleSave}
            disabled={isSaving}
            id={`save-btn-${index}`}
          >
            {isSaving ? (
              <>
                <span className="loading-spinner" style={{ width: 16, height: 16, borderWidth: 2 }} />
                Saving…
              </>
            ) : (
              <>♡ Save to Menu</>
            )}
          </button>
        )}
      </div>
    </article>
  );
}
