package com.example.lab09.recetas

import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface RecipeApiService {
    @GET("recipes")
    suspend fun getRecipes(@Query("limit") limit: Int = 50): RecipesResponse

    @GET("recipes/search")
    suspend fun searchRecipes(@Query("q") texto: String): RecipesResponse

    @GET("recipes/{id}")
    suspend fun getRecipe(@Path("id") id: Int): Recipe
}
