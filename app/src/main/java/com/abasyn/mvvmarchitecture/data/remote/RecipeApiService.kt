package com.abasyn.mvvmarchitecture.data.remote

import retrofit2.http.GET
import retrofit2.http.Query

interface RecipeApiService {

    @GET("list.php")
    suspend fun getIngredients(
        @Query("i") list: String = "list"
    ): IngredientResponse

    @GET("search.php")
    suspend fun getRecentRecipes(
        @Query("s") query: String = ""
    ): RecipeResponse

    @GET("filter.php")
    suspend fun filterRecipesByIngredient(
        @Query("i") ingredient: String
    ): RecipeFilterResponse

    @GET("lookup.php")
    suspend fun getRecipeDetails(
        @Query("i") id: String
    ): RecipeResponse
}