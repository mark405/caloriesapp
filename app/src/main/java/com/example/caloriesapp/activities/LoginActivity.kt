package com.example.caloriesapp.activities

import SharedPreferencesManager.saveAccessToken
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.lifecycle.lifecycleScope
import com.example.caloriesapp.databinding.ActivityLoginBinding
import com.example.caloriesapp.network.LoginResponse
import com.example.caloriesapp.network.RetrofitInstance
import com.example.caloriesapp.network.User
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        RetrofitInstance.init(applicationContext)

        binding.loginButton.setOnClickListener {
            val email = binding.emailEditText.text.toString().trim()
            val password = binding.passwordEditText.text.toString().trim()
            loginUser(email, password)
        }

        binding.registerButton.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }
    }

    private fun loginUser(email: String, password: String) {
        val user = User(email, password)
        val call = RetrofitInstance.authApi.login(user)

        call.enqueue(object : Callback<LoginResponse> {
            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                if (response.isSuccessful) {
                    val accessToken = response.body()?.accessToken
                    if (accessToken != null) {
                        println(accessToken)
                        saveAccessToken(applicationContext, accessToken)
                    }

                    Toast.makeText(applicationContext, "Login successful", Toast.LENGTH_SHORT).show()
                    lifecycleScope.launch {
                        val hasUserOptions = checkIfUserOptionsExist()

                        println(hasUserOptions)

                        try {
                            if (hasUserOptions) {
                                // Go to main activity
                                val intent = Intent(this@LoginActivity, MainActivity::class.java)
                                intent.flags =
                                    Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                                startActivity(intent)
                            } else {
                                println("No user options")
                                val intent =
                                    Intent(this@LoginActivity, UserOptionsActivity::class.java)
                                intent.flags =
                                    Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                                println(intent)
                                startActivity(intent)
                            }
                        } catch (e: Error) {
                            println(e)
                        }

                        finish()
                    }
                } else {
                    println(response)
                    Toast.makeText(applicationContext, "Login failed, response: ${response.message()}", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                Toast.makeText(applicationContext, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private suspend fun checkIfUserOptionsExist(): Boolean {
        return withContext(Dispatchers.IO) {
            try {
                val response = RetrofitInstance.baseApi.getUserDetails()
                response.isSuccessful // Returns true if successful, false otherwise
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(applicationContext, "Error: ${e.localizedMessage}", Toast.LENGTH_SHORT).show()
                }
                false
            }
        }
    }


    private fun logoutUser() {
        val sharedPreferences: SharedPreferences = getSharedPreferences("AppPrefs", MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.remove("userToken")
        editor.apply()

        // Redirect to LoginActivity
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finish()
    }


    private fun saveTokens(accessToken: String?, refreshToken: String?) {
        val sharedPreferences: SharedPreferences = getSharedPreferences("AppPrefs", MODE_PRIVATE)
        val editor = sharedPreferences.edit()

        accessToken?.let { editor.putString("accessToken", it) }
        refreshToken?.let { editor.putString("refreshToken", it) }

        editor.apply()
    }
}
