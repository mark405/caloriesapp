package com.example.caloriesapp.activities

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.caloriesapp.adapters.RecipeSearchAdapter
import com.example.caloriesapp.databinding.ActivitySearchBinding
import com.example.caloriesapp.models.Recipe
import com.example.caloriesapp.network.RetrofitInstance
import kotlinx.coroutines.*

class RecipeSearchActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySearchBinding
    private lateinit var rvAdapter: RecipeSearchAdapter
    private lateinit var dataList: ArrayList<Recipe>
    private var recipes: List<Recipe> = emptyList() // Initialize as empty list

    @OptIn(DelicateCoroutinesApi::class)
    @SuppressLint("ServiceCast")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.search.requestFocus()

        // Use GlobalScope to launch the coroutine
        GlobalScope.launch {
            fetchRecipes()
        }

        binding.goBackHome.setOnClickListener {
            finish()
        }

        // Add text watcher for search input
        binding.search.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s.toString().isNotEmpty()) {
                    filterData(s.toString())
                } else {
                    setupRecyclerView() // Reset to original list when search field is empty
                }
            }

            override fun afterTextChanged(s: Editable?) {}
        })
    }

    // Fetch recipes from API using GlobalScope
    private suspend fun fetchRecipes() {
        try {
            // Directly call the suspend function of Retrofit
            val response = RetrofitInstance.baseApi.getRecipes()

            // Ensure UI updates are on the main thread
            withContext(Dispatchers.Main) {
                // Check if the response is successful
                if (response.isSuccessful && response.body() != null) {
                    recipes = response.body()!!
                    setupRecyclerView() // Set up RecyclerView with fetched recipes
                } else {
                    // Handle the case when the response is not successful
                    Toast.makeText(this@RecipeSearchActivity, "Failed to fetch recipes", Toast.LENGTH_SHORT).show()
                }
            }
        } catch (e: Exception) {
            // Handle any exception that occurs during the network call
            withContext(Dispatchers.Main) {
                Toast.makeText(this@RecipeSearchActivity, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    // Filter recipes based on title
    private fun filterData(filterText: String) {
        val filterData = ArrayList<Recipe>()
        for (recipe in recipes) {
            if (recipe.title.lowercase().contains(filterText.lowercase())) {
                filterData.add(recipe)
            }
        }
        rvAdapter.filterList(filterData) // Update adapter with filtered list
    }

    // Set up RecyclerView with data
    private fun setupRecyclerView() {
        dataList = ArrayList()
        binding.rvSearch.layoutManager = LinearLayoutManager(this)

        for (recipe in recipes) {
            dataList.add(recipe)
        }
        println("popular $dataList")
        rvAdapter = RecipeSearchAdapter(dataList, this)
        binding.rvSearch.adapter = rvAdapter
    }
}
