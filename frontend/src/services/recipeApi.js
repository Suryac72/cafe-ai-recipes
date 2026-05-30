const API_BASE = `${import.meta.env.VITE_API_BASE_URL || 'http://localhost:8089'}/api/recipes`;

function getAuthHeaders() {
  const token = localStorage.getItem('cafe_token');
  const headers = { 'Content-Type': 'application/json' };
  if (token) {
    headers['Authorization'] = `Bearer ${token}`;
  }
  return headers;
}

export async function generateRecipes(ingredients) {
  const response = await fetch(`${API_BASE}/generate`, {
    method: 'POST',
    headers: getAuthHeaders(),
    body: JSON.stringify({ ingredients }),
  });

  if (!response.ok) {
    const errorText = await response.text();
    throw new Error(`Failed to generate recipes: ${errorText}`);
  }

  return response.json();
}

export async function saveRecipe(recipe) {
  const response = await fetch(`${API_BASE}/save`, {
    method: 'POST',
    headers: getAuthHeaders(),
    body: JSON.stringify(recipe),
  });

  if (!response.ok) {
    const errorText = await response.text();
    throw new Error(`Failed to save recipe: ${errorText}`);
  }

  return response.json();
}

export async function getSavedRecipes() {
  const response = await fetch(`${API_BASE}/saved`, {
    headers: getAuthHeaders(),
  });

  if (!response.ok) {
    throw new Error('Failed to fetch saved recipes.');
  }

  return response.json();
}

export async function deleteRecipe(id) {
  const response = await fetch(`${API_BASE}/saved/${id}`, {
    method: 'DELETE',
    headers: getAuthHeaders(),
  });

  if (!response.ok) {
    throw new Error('Failed to delete recipe.');
  }

  return response.json();
}
