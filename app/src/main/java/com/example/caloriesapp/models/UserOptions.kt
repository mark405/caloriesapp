
package com.example.caloriesapp.models

data class UserOptions(
    val userUuid: String,
    val gender: String,
    val height: Int,
    val weight: Float,
    val weightGoal: String,
    val activityLevel: String,
    val age: Int
)