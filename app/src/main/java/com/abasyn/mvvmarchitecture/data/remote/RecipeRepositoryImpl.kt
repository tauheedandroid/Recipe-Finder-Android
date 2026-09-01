package com.abasyn.mvvmarchitecture.data.remote

import com.abasyn.mvvmarchitecture.domain.RecipeRepository
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope

class RecipeRepositoryImpl(
    private val apiService: RecipeApiService
) : RecipeRepository {

    override suspend fun getPopularIngredients(): List<IngredientDto> {
        return apiService
            .getIngredients()
            .meals
            .orEmpty()
    }

    override suspend fun getRecentRecipes(): List<RecipeDto> {
        return apiService
            .getRecentRecipes()
            .meals
            .orEmpty()
    }

    override suspend fun searchRecipesByIngredient(ingredient: String): List<RecipeDto> = coroutineScope {
        try {
            val filterResults = apiService
                .filterRecipesByIngredient(ingredient)
                .meals
                .orEmpty()
            
            val deferredDetails = filterResults.map { filterDto ->
                async {
                    getRecipeDetails(filterDto.idMeal ?: "")
                }
            }
            deferredDetails.awaitAll().filterNotNull()
        } catch (e: Exception) {
            emptyList()
        }
    }

    override suspend fun searchRecipesByIngredients(ingredients: List<String>): List<RecipeDto> = coroutineScope {
        val deferredResults = ingredients.map { ingredient ->
            async {
                searchRecipesByIngredient(ingredient)
            }
        }

        val allResults = deferredResults.awaitAll().flatten()

        // Combine, remove duplicates, and rank by frequency
        allResults
            .groupBy { it.idMeal }
            .map { entry ->
                val recipe = entry.value.first()
                val count = entry.value.size
                recipe to count
            }
            .sortedByDescending { it.second }
            .map { it.first }
    }

    override suspend fun getRecipeDetails(id: String): RecipeDto? {
        return try {
            apiService.getRecipeDetails(id).meals?.firstOrNull()
        } catch (e: Exception) {
            null
        }
    }
}