package com.abasyn.mvvmarchitecture.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.abasyn.mvvmarchitecture.data.remote.IngredientDto
import com.abasyn.mvvmarchitecture.data.remote.RecipeDto
import com.abasyn.mvvmarchitecture.data.remote.RecipeFilterDto
import com.abasyn.mvvmarchitecture.domain.FavoriteRecipeRepository
import com.abasyn.mvvmarchitecture.domain.RecipeRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class HomeViewModel(
    private val repository: RecipeRepository,
    private val favoriteRepository: FavoriteRecipeRepository
) : ViewModel() {

    private val _ingredientsState = MutableStateFlow<IngredientsUiState>(IngredientsUiState.Loading)
    val ingredientsState: StateFlow<IngredientsUiState> = _ingredientsState.asStateFlow()

    private val _recentRecipesState = MutableStateFlow<RecentRecipesUiState>(RecentRecipesUiState.Loading)
    val recentRecipesState: StateFlow<RecentRecipesUiState> = _recentRecipesState.asStateFlow()

    private val _searchState = MutableStateFlow<RecipeSearchState>(RecipeSearchState.Idle)
    val searchState: StateFlow<RecipeSearchState> = _searchState.asStateFlow()

    private val _searchResults = MutableStateFlow<List<RecipeDto>>(emptyList())
    val searchResults: StateFlow<List<RecipeDto>> = _searchResults.asStateFlow()

    val favoriteIds: StateFlow<Set<String>> = favoriteRepository.getAllFavorites()
        .combine(MutableStateFlow(emptySet<String>())) { favorites, _ ->
            favorites.mapNotNull { it.idMeal }.toSet()
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptySet())

    init {
        getPopularIngredients()
        getRecentRecipes()
    }

    fun searchRecipes(query: String) {
        if (query.isBlank()) {
            _searchState.value = RecipeSearchState.Error("Please enter at least one ingredient")
            return
        }

        val ingredients = query.split(Regex("[,\\s]+"))
            .map { it.trim() }
            .filter { it.isNotEmpty() }

        if (ingredients.isEmpty()) {
            _searchState.value = RecipeSearchState.Error("Please enter at least one ingredient")
            return
        }

        viewModelScope.launch {
            _searchState.value = RecipeSearchState.Loading
            try {
                val results = if (ingredients.size == 1) {
                    repository.searchRecipesByIngredient(ingredients[0])
                } else {
                    repository.searchRecipesByIngredients(ingredients)
                }

                if (results.isEmpty()) {
                    _searchState.value = RecipeSearchState.Empty
                } else {
                    _searchResults.value = results
                    _searchState.value = RecipeSearchState.Success(results)
                }
            } catch (e: Exception) {
                _searchState.value = RecipeSearchState.Error(e.message ?: "Something went wrong. Please try again.")
            }
        }
    }

    fun toggleFavorite(recipe: RecipeDto) {
        viewModelScope.launch {
            if (favoriteRepository.isFavoriteSync(recipe.idMeal ?: "")) {
                favoriteRepository.deleteFavorite(recipe.idMeal ?: "")
            } else {
                favoriteRepository.insertFavorite(recipe)
            }
        }
    }

    fun toggleFavoriteFromFilter(recipe: RecipeDto) {
        viewModelScope.launch {
            val id = recipe.idMeal ?: return@launch
            if (favoriteRepository.isFavoriteSync(id)) {
                favoriteRepository.deleteFavorite(id)
            } else {
                favoriteRepository.insertFavorite(recipe)
            }
        }
    }

    fun resetSearchState() {
        _searchState.value = RecipeSearchState.Idle
    }

    fun getPopularIngredients() {
        viewModelScope.launch {
            _ingredientsState.value = IngredientsUiState.Loading
            try {
                val ingredients = repository.getPopularIngredients()
                if (ingredients.isEmpty()) {
                    _ingredientsState.value = IngredientsUiState.Empty
                } else {
                    _ingredientsState.value = IngredientsUiState.Success(ingredients)
                }
            } catch (e: Exception) {
                _ingredientsState.value = IngredientsUiState.Error(e.message ?: "Unknown Error")
            }
        }
    }

    fun getRecentRecipes() {
        viewModelScope.launch {
            _recentRecipesState.value = RecentRecipesUiState.Loading
            try {
                val recipes = repository.getRecentRecipes()
                if (recipes.isEmpty()) {
                    _recentRecipesState.value = RecentRecipesUiState.Empty
                } else {
                    _recentRecipesState.value = RecentRecipesUiState.Success(recipes)
                }
            } catch (e: Exception) {
                _recentRecipesState.value = RecentRecipesUiState.Error(e.message ?: "Unknown Error")
            }
        }
    }

    sealed class IngredientsUiState {
        object Loading : IngredientsUiState()
        data class Success(val data: List<IngredientDto>) : IngredientsUiState()
        data class Error(val message: String) : IngredientsUiState()
        object Empty : IngredientsUiState()
    }

    sealed class RecentRecipesUiState {
        object Loading : RecentRecipesUiState()
        data class Success(val data: List<RecipeDto>) : RecentRecipesUiState()
        data class Error(val message: String) : RecentRecipesUiState()
        object Empty : RecentRecipesUiState()
    }

    sealed interface RecipeSearchState {
        data object Idle : RecipeSearchState
        data object Loading : RecipeSearchState
        data class Success(val recipes: List<RecipeDto>) : RecipeSearchState
        data object Empty : RecipeSearchState
        data class Error(val message: String) : RecipeSearchState
    }
}
