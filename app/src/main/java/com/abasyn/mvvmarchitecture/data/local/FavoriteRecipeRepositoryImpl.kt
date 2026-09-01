package com.abasyn.mvvmarchitecture.data.local

import com.abasyn.mvvmarchitecture.data.remote.RecipeDto
import com.abasyn.mvvmarchitecture.domain.FavoriteRecipeRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class FavoriteRecipeRepositoryImpl(
    private val favoriteRecipeDao: FavoriteRecipeDao
) : FavoriteRecipeRepository {

    override suspend fun insertFavorite(recipe: RecipeDto) {
        favoriteRecipeDao.insertFavorite(recipe.toEntity())
    }

    override suspend fun deleteFavorite(recipeId: String) {
        favoriteRecipeDao.deleteFavorite(recipeId)
    }

    override fun getAllFavorites(): Flow<List<RecipeDto>> {
        return favoriteRecipeDao.getAllFavorites().map { entities ->
            entities.map { it.toDto() }
        }
    }

    override fun isFavorite(recipeId: String): Flow<Boolean> {
        return favoriteRecipeDao.isFavorite(recipeId)
    }

    override suspend fun isFavoriteSync(recipeId: String): Boolean {
        return favoriteRecipeDao.isFavoriteSync(recipeId)
    }
}
