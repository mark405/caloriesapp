package com.example.caloriesapp.activities

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.caloriesapp.R
import com.example.caloriesapp.databinding.ActivityMainBinding
import com.example.caloriesapp.fragments.DiaryFragment
import com.example.caloriesapp.fragments.RecipesFragment
import com.example.caloriesapp.fragments.SettingsFragment
import org.json.JSONObject
import android.util.Base64
import com.example.caloriesapp.databinding.ActivityReceiptBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Check if the user is logged in
        if (!isUserLoggedIn()) {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
            return
        }
        // Set up view binding
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)



        // Set default fragment
        replaceFragment(RecipesFragment())

        // Handle bottom navigation
        setupBottomNavigation()
    }

    private fun setupBottomNavigation() {
        binding.bottomNavigation.setOnItemSelectedListener { item ->
            val selectedFragment: Fragment = when (item.itemId) {
                R.id.menu_recipes -> RecipesFragment()
                R.id.menu_diary -> DiaryFragment()
                R.id.menu_reports -> SettingsFragment()
                else -> RecipesFragment()
            }
            replaceFragment(selectedFragment)
            true
        }
    }

    private fun replaceFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.nav_host_fragment, fragment) // Ensure nav_host_fragment is defined
            .commit()
    }

    private fun isUserLoggedIn(): Boolean {
        val sharedPreferences: SharedPreferences = getSharedPreferences("AppPrefs", MODE_PRIVATE)
        val token = sharedPreferences.getString("accessToken", null)

        // If the token is null or empty, the user is not logged in
        if (token.isNullOrEmpty()) {
            return false
        }

        // Check if the token has expired
        return !isTokenExpired(token)
    }

    private fun isTokenExpired(token: String): Boolean {
        try {
            // Split the token into parts (Header, Payload, Signature)
            val parts = token.split(".")
            if (parts.size < 2) {
                return true // Invalid token
            }

            // Decode the payload (second part) from Base64
            val payload = String(Base64.decode(parts[1], Base64.URL_SAFE))
            val jsonObject = JSONObject(payload)

            // Extract the expiration time (exp) from the payload
            val exp = jsonObject.optLong("exp", 0)

            // Check if the expiration time has passed
            val currentTimeInSeconds = System.currentTimeMillis() / 1000
            return exp < currentTimeInSeconds
        } catch (e: Exception) {
            e.printStackTrace()
            return true // Assume expired if there's an error
        }
    }

}
