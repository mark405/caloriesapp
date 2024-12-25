package com.example.caloriesapp.models

import java.util.Date
import java.util.Optional

data class Meal(
    val title: String,
    val fats: Int,
    val proteins: Int,
    val calories: Int,
    val carbs: Int,
    val mealType: String,
    val weight: Float,
    val date: Date
)
