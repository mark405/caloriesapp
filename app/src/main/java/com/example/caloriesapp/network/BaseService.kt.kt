package com.example.caloriesapp.network

import com.example.caloriesapp.Recipe
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface BaseService {
    @POST("/api/save-user-options")
    suspend fun saveUserOptions(@Body options: Map<String, String>): Response<Void>

    @GET("/api/recipes")
    suspend fun getRecipes(): Response<List<Recipe>>}
