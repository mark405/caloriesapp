package com.example.caloriesapp.network

import android.content.Context
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {
    private const val AUTH_URL = "http://10.0.2.2:8000"
    private const val BASE_URL = "http://10.0.2.2:8000"

    var appContext: Context? = null

    fun init(context: Context) {
        appContext = context.applicationContext
    }

    private val authClient = OkHttpClient.Builder().apply {
        addInterceptor { chain ->
            val request = chain.request().newBuilder()
            val token = appContext?.let { SharedPreferencesManager.getAccessToken(it) }
            token?.let {
                request.addHeader("Authorization", "Bearer $it")
            }
            chain.proceed(request.build())
        }
    }.build()

    private val baseClient = OkHttpClient.Builder().apply {
        addInterceptor { chain ->
            val request = chain.request().newBuilder()
            val token = appContext?.let { SharedPreferencesManager.getAccessToken(it) }
            token?.let {
                request.addHeader("Authorization", "Bearer $it")
            }
            chain.proceed(request.build())
        }
    }.build()
    private val authRetrofit = Retrofit.Builder()
        .baseUrl(AUTH_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .client(authClient)
        .build()

    private val baseRetrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .client(baseClient)
        .build()

    val authApi: AuthService = authRetrofit.create(AuthService::class.java)
    val baseApi: BaseService = baseRetrofit.create(BaseService::class.java)
}
