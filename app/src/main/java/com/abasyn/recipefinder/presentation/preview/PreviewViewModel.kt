package com.abasyn.recipefinder.presentation.preview

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.abasyn.recipefinder.data.remote.RecipeDto
import com.abasyn.recipefinder.domain.FavoriteRecipeRepository
import com.abasyn.recipefinder.domain.RecipeRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.launch

class PreviewViewModel(
    private val repository: RecipeRepository,
    private val favoriteRepository: FavoriteRecipeRepository
) : ViewModel() {

    private val _recipe = MutableLiveData<RecipeDto?>()
    val recipe: LiveData<RecipeDto?> = _recipe

    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> = _loading

    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> = _error

    private val recipeIdFlow = MutableStateFlow<String?>(null)
    
    @OptIn(ExperimentalCoroutinesApi::class)
    val isFavorite: LiveData<Boolean> = recipeIdFlow.flatMapLatest { id ->
        if (id == null) {
            kotlinx.coroutines.flow.flowOf(false)
        } else {
            favoriteRepository.isFavorite(id)
        }
    }.asLiveData()

    fun getRecipeDetails(id: String) {
        recipeIdFlow.value = id
        _loading.value = true
        viewModelScope.launch {
            try {
                val result = repository.getRecipeDetails(id)
                _recipe.value = result
                if (result == null) {
                    _error.value = "Recipe not found"
                }
            } catch (e: Exception) {
                _error.value = e.message ?: "An error occurred"
            } finally {
                _loading.value = false
            }
        }
    }

    fun toggleFavorite() {
        val currentRecipe = _recipe.value ?: return
        viewModelScope.launch {
            if (favoriteRepository.isFavoriteSync(currentRecipe.idMeal ?: "")) {
                favoriteRepository.deleteFavorite(currentRecipe.idMeal ?: "")
            } else {
                favoriteRepository.insertFavorite(currentRecipe)
            }
        }
    }
}
