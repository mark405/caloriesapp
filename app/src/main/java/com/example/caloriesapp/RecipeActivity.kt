package com.example.caloriesapp

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.annotation.RequiresApi
import com.bumptech.glide.Glide
import com.example.caloriesapp.databinding.ActivityRecipeBinding

class RecipeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRecipeBinding
    private var isImageCropped = true

    @SuppressLint("SetTextI18n")
    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRecipeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Load image using Glide
        Glide.with(this).load(intent.getStringExtra("img")).into(binding.itemImg)

        // Set title and description
        binding.tittle.text = intent.getStringExtra("title")
        binding.stepData.text = intent.getStringExtra("des")

        // Handle ingredients
        val ingredients = intent.getStringExtra("ing")?.split(",")?.filter { it.isNotEmpty() }?.toTypedArray()
        binding.time.text = intent.getStringExtra("des")

        // Display ingredients in the list
        ingredients?.forEach { ingredient ->
            binding.ingData.append("🟢 $ingredient\n")
        }

        // Default background for step description
        binding.step.background = null
        binding.step.setTextColor(getColor(R.color.black))

        // Toggle between ingredients and steps
        binding.step.setOnClickListener {
            binding.step.setBackgroundResource(R.drawable.btn_ing)
            binding.step.setTextColor(getColor(R.color.white))
            binding.ing.setTextColor(getColor(R.color.black))
            binding.ing.background = null
            binding.stepScroll.visibility = View.VISIBLE
            binding.ingScroll.visibility = View.GONE
        }

        binding.ing.setOnClickListener {
            binding.ing.setBackgroundResource(R.drawable.btn_ing)
            binding.ing.setTextColor(getColor(R.color.white))
            binding.step.setTextColor(getColor(R.color.black))
            binding.step.background = null
            binding.ingScroll.visibility = View.VISIBLE
            binding.stepScroll.visibility = View.GONE
        }

        // Toggle image scale type
        binding.fullScreen.setOnClickListener {
            if (isImageCropped) {
                binding.itemImg.scaleType = ImageView.ScaleType.FIT_CENTER
                Glide.with(this).load(intent.getStringExtra("img")).into(binding.itemImg)
                binding.fullScreen.setColorFilter(Color.BLACK)
                binding.shade.visibility = View.GONE
            } else {
                binding.itemImg.scaleType = ImageView.ScaleType.CENTER_CROP
                Glide.with(this).load(intent.getStringExtra("img")).into(binding.itemImg)
                binding.fullScreen.setColorFilter(null)
                binding.shade.visibility = View.VISIBLE
            }
            isImageCropped = !isImageCropped
        }

        // Back button functionality
        binding.backBtn.setOnClickListener {
            finish()
        }
    }
}
