package com.example.caloriesapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.caloriesapp.databinding.ActivityCategoryBinding
import com.example.caloriesapp.network.RetrofitInstance
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class CategoryActivity : AppCompatActivity() {

    private lateinit var rvAdapter: CategoryAdapter
    private lateinit var dataList: ArrayList<Recipe>
    private val binding by lazy {
        ActivityCategoryBinding.inflate(layoutInflater)
    }
    private var recipes: List<Recipe> = emptyList() // Initialize as empty list

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        title = intent.getStringExtra("TITTLE")
        GlobalScope.launch {
            fetchRecipes()
        }
        setupRecyclerView()
        binding.goBackHome.setOnClickListener {
            finish()
        }

    }

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
                    Toast.makeText(this@CategoryActivity, "Failed to fetch recipes", Toast.LENGTH_SHORT).show()
                }
            }
        } catch (e: Exception) {
            // Handle any exception that occurs during the network call
            withContext(Dispatchers.Main) {
                Toast.makeText(this@CategoryActivity, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }


    private fun setupRecyclerView() {

        dataList = ArrayList()
        binding.rvCategory.layoutManager =
            LinearLayoutManager(this)

        for (i in recipes.indices) {
            if (recipes[i].mealType == intent.getStringExtra("CATEGORY")!!) {
                dataList.add(recipes[i])
            }
            rvAdapter = CategoryAdapter(dataList, this)
            binding.rvCategory.adapter = rvAdapter
        }
    }

}