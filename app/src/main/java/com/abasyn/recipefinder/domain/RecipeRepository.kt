package com.abasyn.recipefinder.domain

import com.abasyn.recipefinder.data.remote.IngredientDto
import com.abasyn.recipefinder.data.remote.RecipeDto

interface RecipeRepository {

    suspend fun getPopularIngredients(): List<IngredientDto>

    suspend fun getRecentRecipes(): List<RecipeDto>

    suspend fun searchRecipesByIngredient(
        ingredient: String
    ): List<RecipeDto>

    suspend fun searchRecipesByIngredients(
        ingredients: List<String>
    ): List<RecipeDto>

    suspend fun getRecipeDetails(
        id: String
    ): RecipeDto?
}