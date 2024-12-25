package com.example.caloriesapp.activities

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.caloriesapp.R

class AboutAppActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_about_app)

        // Handle buttons for navigating to app sections
        findViewById<ImageView>(R.id.iv_diary).setOnClickListener {
            Toast.makeText(this, "Navigate to Diary", Toast.LENGTH_SHORT).show()
            // Add logic to navigate to Diary
        }

        // Navigate to Reports section
        findViewById<ImageView>(R.id.iv_reports).setOnClickListener {
            Toast.makeText(this, "Navigate to Reports", Toast.LENGTH_SHORT).show()
            // Add logic to navigate to Reports
        }

        // Navigate to Recipes section
        findViewById<ImageView>(R.id.iv_recipes).setOnClickListener {
            Toast.makeText(this, "Navigate to Recipes", Toast.LENGTH_SHORT).show()
            // Add logic to navigate to Recipes
        }

        // Handle source code button
        findViewById<ImageView>(R.id.iv_source_code).setOnClickListener {
            val githubUrl = "https://github.com/mark405/caloriesapp" // Replace with your actual GitHub repo
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(githubUrl))
            startActivity(intent)
        }
    }
}
