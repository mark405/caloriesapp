package com.example.caloriesapp.network

import android.content.Context
import android.util.Log
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {
    private const val AUTH_URL = "https://f30f-178-94-39-75.ngrok-free.app"
    private const val BASE_URL = "https://f30f-178-94-39-75.ngrok-free.app"

    var appContext: Context? = null

    /**
     * Initializes the RetrofitInstance with the application context.
     * This must be called once in the Application class.
     */
    fun init(context: Context) {
        appContext = context.applicationContext
    }

    // Auth client for requests requiring authentication
    private val authClient = OkHttpClient.Builder().apply {
        addInterceptor { chain ->
            val token = appContext?.let { SharedPreferencesManager.getAccessToken(it) }

            // Log token for debugging
            Log.d("AuthInterceptor", "Access Token: $token")

            val request = chain.request().newBuilder().apply {
                token?.let { addHeader("Authorization", "Bearer $it") }
            }.build()

            chain.proceed(request)
        }
    }.build()

    // Base client for general requests
    private val baseClient = OkHttpClient.Builder().apply {
        addInterceptor { chain ->
            val token = appContext?.let {
                Log.d("Interceptor", "AppContext available: $it")
                SharedPreferencesManager.getAccessToken(it)
            }

            // Log token status
            if (token == null) {
                Log.e("Interceptor", "Token is null!")
            } else {
                Log.d("Interceptor", "Token retrieved: $token")
            }

            val requestBuilder = chain.request().newBuilder()
            token?.let { requestBuilder.addHeader("Authorization", "Bearer $it") }

            val request = requestBuilder.build()
            Log.d("Interceptor", "Request Headers: ${request.headers()}")
            chain.proceed(request)
        }
    }.build()


    // Retrofit instance for authenticated requests
    private val authRetrofit: Retrofit = Retrofit.Builder()
        .baseUrl(AUTH_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .client(authClient)
        .build()

    // Retrofit instance for general requests
    private val baseRetrofit: Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .client(baseClient)
        .build()

    val authApi: AuthService = authRetrofit.create(AuthService::class.java)
    val baseApi: BaseService = baseRetrofit.create(BaseService::class.java)
}
