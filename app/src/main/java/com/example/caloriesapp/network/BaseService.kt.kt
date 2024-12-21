package com.example.caloriesapp.network

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface BaseService {
    @POST("/api/save-user-options")
    suspend fun saveUserOptions(@Body options: Map<String, String>): Response<Void>
}
