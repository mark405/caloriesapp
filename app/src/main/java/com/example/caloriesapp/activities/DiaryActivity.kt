package com.example.caloriesapp.activities

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.caloriesapp.R
import com.example.caloriesapp.adapters.MealAdapter
import com.example.caloriesapp.network.RetrofitInstance
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class DiaryActivity : AppCompatActivity() {

    private lateinit var mealsRecyclerView: RecyclerView
    private lateinit var mealAdapter: MealAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_diary)

        mealsRecyclerView = findViewById(R.id.meals_recycler_view)
        mealAdapter = MealAdapter(emptyList())
        mealsRecyclerView.layoutManager = LinearLayoutManager(this)
        mealsRecyclerView.adapter = mealAdapter

        fetchMeals()
    }

    private fun fetchMeals() {
        CoroutineScope(Dispatchers.IO).launch {
            val response = RetrofitInstance.baseApi.getMeals()
            withContext(Dispatchers.Main) {
                if (response.isSuccessful) {
                    response.body()?.let { meals ->
                        mealAdapter.updateMeals(meals)
                    }
                } else {
                    Toast.makeText(this@DiaryActivity, "Error fetching meals", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}
