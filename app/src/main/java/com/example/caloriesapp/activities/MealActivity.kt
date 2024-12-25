package com.example.caloriesapp.activities

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContentProviderCompat.requireContext
import com.example.caloriesapp.R
import com.example.caloriesapp.network.RetrofitInstance
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.Locale

class MealActivity : AppCompatActivity() {

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_meal_overview)

        findViewById<Button>(R.id.btn_delete_meal).setOnClickListener {
            intent.getStringExtra("mealUuid")?.let { mealId ->
                deleteMeal(mealId)
            }
        }

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
        findViewById<TextView>(R.id.meal_overview_title).text = mealTitle
        findViewById<TextView>(R.id.mealCalories).text = "$mealCalories"
        findViewById<TextView>(R.id.meal_proteins).text = "$mealProteins g "
        findViewById<TextView>(R.id.meal_fats).text = "$mealFats g "
        findViewById<TextView>(R.id.meal_carbs).text = "$mealCarbs g "
        findViewById<TextView>(R.id.meal_weight).text = "$mealWeight g weight"
        println(mealDate)
        val inputFormat = SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.ENGLISH)
        val outputFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

        try {
            val date = inputFormat.parse(mealDate) // Parse the date from the given format
            val formattedDate = outputFormat.format(date) // Reformat to "yyyy-MM-dd"
            findViewById<TextView>(R.id.meal_date).text = "Date: $formattedDate"
        } catch (e: Exception) {
            e.printStackTrace()
            findViewById<TextView>(R.id.meal_date).text = "Date: Invalid"
        }
       }

    private fun deleteMeal(mealId: String) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = RetrofitInstance.baseApi.deleteMeal(mealId)
                withContext(Dispatchers.Main) {
                    if (response.isSuccessful) {
                        Toast.makeText(applicationContext, "Meal deleted successfully!", Toast.LENGTH_SHORT).show()
                        finish() // Close the activity and return to the previous screen
                    } else {
                        Toast.makeText(applicationContext, "Failed to delete meal: ${response.message()}", Toast.LENGTH_SHORT).show()
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(applicationContext, "Error: ${e.localizedMessage}", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
    }
