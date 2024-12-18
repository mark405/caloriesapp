package com.example.caloriesapp.network

import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

data class User(val email: String, val password: String)

interface AuthService {
    @POST("/auth/login")
    fun login(@Body user: User): Call<LoginResponse>

    @POST("/auth/register")
    fun register(@Body user: User): Call<RegisterResponse>

    @POST("/user/options")
    suspend fun saveUserOptions(@Body options: Map<String, String>): Response<Void>

}

// Define response data classes
data class LoginResponse(val accessToken: String, val refreshToken: String)
data class RegisterResponse(val accessToken: String, val refreshToken: String)
