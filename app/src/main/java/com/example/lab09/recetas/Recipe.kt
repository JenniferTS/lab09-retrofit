package com.example.lab09.recetas

import com.google.gson.annotations.SerializedName

// Modelo de una receta de https://dummyjson.com/recipes
data class Recipe(
    @SerializedName("id") val id: Int,
    @SerializedName("name") val name: String,
    @SerializedName("ingredients") val ingredients: List<String>,
    @SerializedName("instructions") val instructions: List<String>,
    @SerializedName("prepTimeMinutes") val prepTimeMinutes: Int,
    @SerializedName("cookTimeMinutes") val cookTimeMinutes: Int,
    @SerializedName("servings") val servings: Int,
    @SerializedName("difficulty") val difficulty: String,
    @SerializedName("cuisine") val cuisine: String,
    @SerializedName("caloriesPerServing") val calories: Int,
    @SerializedName("image") val image: String,
    @SerializedName("rating") val rating: Double,
    @SerializedName("mealType") val mealType: List<String>
)

// La API devuelve la lista dentro de un objeto: { "recipes": [...], "total": 50, ... }
data class RecipesResponse(
    @SerializedName("recipes") val recipes: List<Recipe>,
    @SerializedName("total") val total: Int
)
