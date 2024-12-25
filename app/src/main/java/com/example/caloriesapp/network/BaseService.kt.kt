package com.example.caloriesapp.network

import com.example.caloriesapp.fragments.WeightEntry
import com.example.caloriesapp.models.Meal
import com.example.caloriesapp.models.Recipe
import com.example.caloriesapp.models.UserMacros
import com.example.caloriesapp.models.UserOptions
import com.example.caloriesapp.models.WaterIntake
import com.example.caloriesapp.models.Weight
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

data class UserDetails(
    val email: String,
    val calorieIntake: Int,
    val gender: String,
    val age: String,
    val weight: String,
    val height: String,
    val activityLevel: String,
    val weightGoal: String
)

interface BaseService {
    @POST("/api/save-user-options")
    suspend fun saveUserOptions(@Body options: Map<String, String>): Response<Void>

    @POST("/api/update-user-options")
    suspend fun updateUserOptions(@Body options: Map<String, String>): Response<Void>

    @GET("/api/get-user-options")
    suspend fun getUserDetails(): Response<UserDetails>

    @GET("/api/recipes")
    suspend fun getRecipes(): Response<List<Recipe>>

    @GET("/api/meals")
    suspend fun getMeals(): Response<List<Meal>>

    @DELETE("/api/meals/{id}")
    suspend fun deleteMeal(@Path("id") id: String): Response<Void>

    @POST("/api/create_meal")
    suspend fun createMeal(@Body meal: Meal): Response<Meal>

    @GET("/api/water_intakes")
    suspend fun getWaterIntakes(): Response<List<WaterIntake>>

    @POST("/api/add_water_intake")
    suspend fun addWaterIntake(@Body options: Map<String, String>): Response<Void>

    @DELETE("api/delete_water_intake/{water_intake_id}")
    suspend fun deleteWaterIntake(@Path("water_intake_id") waterIntakeId: String): Response<Void>

    @GET("/api/recommended_water_intake")
    suspend fun getRecommendedWaterIntake(): Response<WaterIntake>

    @POST("/api/add_weight")
    suspend fun addWeight(@Body weightEntry: WeightEntry): Response<Void>

    @GET("/api/weights")
    suspend fun getWeights(): Response<List<WeightEntry>>

    @POST("/api/update-user_macros")
    suspend fun updateUserMacros(@Body options: Map<String, String>): Response<Void>

    @GET("api/get-user-macros")
    suspend fun getUserMacros(): Response<UserMacros>

    @GET("/api/recommended-user-macros")
    suspend fun getRecommendedUserMacros(): Response<UserMacros>
}
