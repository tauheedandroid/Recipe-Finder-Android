package com.abasyn.recipefinder.presentation.favorite

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.abasyn.recipefinder.data.remote.RecipeDto
import com.abasyn.recipefinder.domain.FavoriteRecipeRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class FavoriteViewModel(
    private val favoriteRepository: FavoriteRecipeRepository
) : ViewModel() {

    val favoriteRecipes: StateFlow<List<RecipeDto>> = favoriteRepository.getAllFavorites()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    fun toggleFavorite(recipe: RecipeDto) {
        viewModelScope.launch {
            if (favoriteRepository.isFavoriteSync(recipe.idMeal ?: "")) {
                favoriteRepository.deleteFavorite(recipe.idMeal ?: "")
            } else {
                favoriteRepository.insertFavorite(recipe)
            }
        }
    }

    fun removeFavorite(recipeId: String) {
        viewModelScope.launch {
            favoriteRepository.deleteFavorite(recipeId)
        }
    }
}
