package com.example.caloriesapp.activities

import com.example.caloriesapp.adapters.PopularAdapter
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.caloriesapp.databinding.ActivityPopularBinding
import com.example.caloriesapp.models.Recipe
import com.example.caloriesapp.network.RetrofitInstance
import kotlinx.coroutines.launch

class PopularActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPopularBinding
    private lateinit var rvAdapter: PopularAdapter
    private lateinit var dataList: ArrayList<Recipe>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityPopularBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Setup RecyclerView
        setupRecyclerView()

        // Setup toolbar as ActionBar
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding.toolbar.setNavigationOnClickListener {
            onBackPressed() // Handle the back action
        }
    }

    private fun setupRecyclerView() {
        dataList = ArrayList()
        binding.rvPopular.layoutManager = LinearLayoutManager(this)

        lifecycleScope.launch {
            try {
                val response = RetrofitInstance.baseApi.getRecipes() // Use your endpoint here
                if (response.isSuccessful) {
                    val recipes = response.body() ?: emptyList()
                    if (recipes.isNotEmpty()) {
                        println("ADASDASDASDASDASDASDASDASDAS")

                        dataList.addAll(recipes)
                        rvAdapter = PopularAdapter(dataList, this@PopularActivity)
                        binding.rvPopular.adapter = rvAdapter
                        rvAdapter.notifyDataSetChanged()
                    } else {
                        Toast.makeText(this@PopularActivity, "No popular recipes found", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(this@PopularActivity, "Failed to load popular recipes", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                Toast.makeText(this@PopularActivity, "Error occurred: ${e.localizedMessage}", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
