package com.example.caloriesapp.network

import android.content.Context
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {
    private const val BASE_URL = "https://browney-auth-1033066542238.europe-west1.run.app"

    var appContext: Context? = null

    fun init(context: Context) {
        appContext = context.applicationContext
    }

    private val client = OkHttpClient.Builder().apply {
        addInterceptor { chain ->
            val request = chain.request().newBuilder()
            val token = appContext?.let { SharedPreferencesManager.getAccessToken(it) }
            token?.let {
                request.addHeader("Authorization", "Bearer $it")
            }
            chain.proceed(request.build())
        }
    }.build()

    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .client(client)
        .build()

    val api: AuthService = retrofit.create(AuthService::class.java)
}
