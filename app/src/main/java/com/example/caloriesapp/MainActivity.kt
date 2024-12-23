package com.example.caloriesapp

import PopularAdapter
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.caloriesapp.databinding.ActivityMainBinding
import com.example.caloriesapp.databinding.ActivityReceiptBinding
import com.example.caloriesapp.network.RetrofitInstance
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var bindingReceipt: ActivityReceiptBinding
    private lateinit var rvAdapter: PopularAdapter
    private lateinit var dataList: ArrayList<Recipe>


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (!isUserLoggedIn()) {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
            return
        }

        binding = ActivityMainBinding.inflate(layoutInflater)
        bindingReceipt = ActivityReceiptBinding.inflate(layoutInflater)

        setContentView(binding.root)

        setupRecyclerView()
        binding.mainLayout.addView(bindingReceipt.root)

        bindingReceipt.search.setOnClickListener {
            startActivity(Intent(this, RecipeSearchActivity::class.java))
        }

        bindingReceipt.salad.setOnClickListener {
            var intent = Intent(this@MainActivity, CategoryActivity::class.java)
            intent.putExtra("TITTLE", "Сніданок")
            intent.putExtra("CATEGORY", "Breakfast")
            startActivity(intent)
        }
        bindingReceipt.mainDish.setOnClickListener {
            var intent = Intent(this@MainActivity, CategoryActivity::class.java)
            intent.putExtra("TITTLE", "Обід")
            intent.putExtra("CATEGORY", "Lunch")
            startActivity(intent)
        }
        bindingReceipt.drinks.setOnClickListener {
            var intent = Intent(this@MainActivity, CategoryActivity::class.java)
            intent.putExtra("TITTLE", "Вечеря")
            intent.putExtra("CATEGORY", "Dinner")
            startActivity(intent)
        }
        bindingReceipt.desserts.setOnClickListener {
            var intent = Intent(this@MainActivity, CategoryActivity::class.java)
            intent.putExtra("TITTLE", "Снеки")
            intent.putExtra("CATEGORY", "Snack")
            startActivity(intent)
        }

    }

    private fun isUserLoggedIn(): Boolean {
        val sharedPreferences: SharedPreferences = getSharedPreferences("AppPrefs", MODE_PRIVATE)
        val token = sharedPreferences.getString("accessToken", null)
        return !token.isNullOrEmpty()
    }
    private fun logout() {
        // Clear the stored token
        val sharedPreferences: SharedPreferences = getSharedPreferences("AppPrefs", MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.remove("accessToken")  // Remove the access token
        editor.apply()

        // Redirect to LoginActivity
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finish()  // Finish current activity to remove it from back stack
    }

    @OptIn(DelicateCoroutinesApi::class)
    private fun setupRecyclerView() {
        dataList = ArrayList()
        binding.rvPopular.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)

        GlobalScope.launch(Dispatchers.Main) {
            try {
                val response = RetrofitInstance.baseApi.getRecipes()
                if (response.isSuccessful) {
                    val recipes = response.body() ?: emptyList()
                    val popularRecipes = recipes.filter { it.isPopular }

                    if (popularRecipes.isNotEmpty()) {
                        dataList.addAll(popularRecipes)
                        rvAdapter = PopularAdapter(dataList, this@MainActivity)
                        binding.rvPopular.adapter = rvAdapter
                        rvAdapter.notifyDataSetChanged()
                    } else {
                        Toast.makeText(this@MainActivity, "No recipes found", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(this@MainActivity, "Failed to load recipes: ${response.message()}", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                Toast.makeText(this@MainActivity, "Error occurred: ${e.localizedMessage}", Toast.LENGTH_SHORT).show()
            }
        }

    }


}
