package com.abasyn.mvvmarchitecture.domain

import com.abasyn.mvvmarchitecture.data.remote.IngredientDto
import com.abasyn.mvvmarchitecture.data.remote.RecipeDto
import com.abasyn.mvvmarchitecture.data.remote.RecipeFilterDto

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