package com.example.caloriesapp.activities

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Html
import android.text.Spanned
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.RequiresApi
import com.bumptech.glide.Glide
import com.example.caloriesapp.R

class RecipeActivity : AppCompatActivity() {

    private var isImageCropped = true

    @SuppressLint("SetTextI18n")
    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recipe)

        // Initialize views
        val recipeImage: ImageView = findViewById(R.id.recipe_image)
        val recipeTitle: TextView = findViewById(R.id.recipe_title)
        val recipeTime: TextView = findViewById(R.id.recipe_time)
        val recipeCalories: TextView = findViewById(R.id.recipe_calories)
        val recipeProteins: TextView = findViewById(R.id.recipe_proteins)
        val recipeFats: TextView = findViewById(R.id.recipe_fats)
        val recipeCarbs: TextView = findViewById(R.id.recipe_carbs)
        val recipeDescription = findViewById<TextView>(R.id.recipe_description)
        //        val fullScreenToggle: ImageView = findViewById(R.id.button) // Add this button in your XML
//        val backButton: ImageView = findViewById(R.id.back_btn) // Add this button in your XML

        // Load image using Glide
        Glide.with(this).load(intent.getStringExtra("img")).into(recipeImage)

        // Set title and description
        recipeTitle.text = intent.getStringExtra("title")
        recipeDescription.text = intent.getStringExtra("des")

        val spanned: Spanned = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            Html.fromHtml(intent.getStringExtra("des"), Html.FROM_HTML_MODE_LEGACY)
        } else {
            Html.fromHtml(intent.getStringExtra("des"))
        }

        recipeDescription.text = spanned

        // Set time and nutritional information
        recipeCalories.text = "${intent.getIntExtra("calories", 0)} Calories"
        recipeProteins.text = "${intent.getIntExtra("proteins", 0)}g Proteins"
        recipeFats.text = "${intent.getIntExtra("fats", 0)}g Fats"
        recipeCarbs.text = "${intent.getIntExtra("carbs", 0)}g Carbs"
        recipeTime.text = "${intent.getStringExtra("time")}"
        val fullScreenToggle: ImageView = findViewById(R.id.full_screen_button)
        val backButton: ImageView = findViewById(R.id.back_btn) // Add this button in your XML
        // Toggle image scale type
        fullScreenToggle.setOnClickListener {
            if (isImageCropped) {
                recipeImage.scaleType = ImageView.ScaleType.FIT_CENTER
                Glide.with(this).load(intent.getStringExtra("img")).into(recipeImage)
                fullScreenToggle.setColorFilter(Color.BLACK)
            } else {
                recipeImage.scaleType = ImageView.ScaleType.CENTER_CROP
                Glide.with(this).load(intent.getStringExtra("img")).into(recipeImage)
                fullScreenToggle.setColorFilter(null)
            }
            isImageCropped = !isImageCropped
        }

        // Back button functionality
        backButton.setOnClickListener {
            finish()
        }
    }
}
