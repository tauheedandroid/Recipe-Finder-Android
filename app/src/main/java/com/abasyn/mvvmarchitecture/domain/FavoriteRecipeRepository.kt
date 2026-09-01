package com.abasyn.mvvmarchitecture.domain

import com.abasyn.mvvmarchitecture.data.remote.RecipeDto
import kotlinx.coroutines.flow.Flow

interface FavoriteRecipeRepository {
    suspend fun insertFavorite(recipe: RecipeDto)
    suspend fun deleteFavorite(recipeId: String)
    fun getAllFavorites(): Flow<List<RecipeDto>>
    fun isFavorite(recipeId: String): Flow<Boolean>
    suspend fun isFavoriteSync(recipeId: String): Boolean
}
