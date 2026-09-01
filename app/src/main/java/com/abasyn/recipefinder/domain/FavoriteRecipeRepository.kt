package com.abasyn.recipefinder.domain

import com.abasyn.recipefinder.data.remote.RecipeDto
import kotlinx.coroutines.flow.Flow

interface FavoriteRecipeRepository {
    suspend fun insertFavorite(recipe: RecipeDto)
    suspend fun deleteFavorite(recipeId: String)
    fun getAllFavorites(): Flow<List<RecipeDto>>
    fun isFavorite(recipeId: String): Flow<Boolean>
    suspend fun isFavoriteSync(recipeId: String): Boolean
}
