
package com.example.caloriesapp.models

data class Recipe(
    val uuid: String,
    val proteins: Long,
    val fats: Long,
    val calories: Long,
    val carbs: Long,
    val title: String,
    val description: String,
    val cookingTime: String,
    val isPopular: Boolean,
    val coverImage: String,
    val mealType: String
)