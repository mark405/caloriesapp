package com.example.caloriesapp.activities

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.caloriesapp.R

class MealActivity : AppCompatActivity() {

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_meal)

        // Get data from intent
        val mealTitle = intent.getStringExtra("mealTitle")
        val mealType = intent.getStringExtra("mealType")
        val mealCalories = intent.getIntExtra("mealCalories", 0)
        val mealProteins = intent.getIntExtra("mealProteins", 0)
        val mealFats = intent.getIntExtra("mealFats", 0)
        val mealCarbs = intent.getIntExtra("mealCarbs", 0)
        val mealWeight = intent.getFloatExtra("mealWeight", 0f)
        val mealDate = intent.getStringExtra("mealDate")

        // Set data to views
        findViewById<TextView>(R.id.meal_title).text = mealTitle
        findViewById<TextView>(R.id.meal_type).text = "Type: $mealType"
        findViewById<TextView>(R.id.meal_calories).text = "$mealCalories Calories"
        findViewById<TextView>(R.id.meal_proteins).text = "$mealProteins g Proteins"
        findViewById<TextView>(R.id.meal_fats).text = "$mealFats g Fats"
        findViewById<TextView>(R.id.meal_carbs).text = "$mealCarbs g Carbs"
        findViewById<TextView>(R.id.meal_weight).text = "$mealWeight g"
        findViewById<TextView>(R.id.meal_date).text = "Date: $mealDate"
    }
}
