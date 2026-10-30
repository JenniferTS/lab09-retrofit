package com.example.lab09.recetas

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

// Capa de datos: el unico lugar que sabe que los datos vienen de Retrofit
object RecipeRepository {
    private val api: RecipeApiService = Retrofit.Builder()
        .baseUrl("https://dummyjson.com/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(RecipeApiService::class.java)

    suspend fun getRecipes(): List<Recipe> = api.getRecipes().recipes

    suspend fun search(texto: String): List<Recipe> =
        if (texto.isBlank()) getRecipes() else api.searchRecipes(texto.trim()).recipes

    suspend fun getRecipe(id: Int): Recipe = api.getRecipe(id)
}
