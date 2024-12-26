package com.example.caloriesapp.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater.*
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.caloriesapp.models.Recipe
import com.example.caloriesapp.activities.RecipeActivity
import com.example.caloriesapp.databinding.PopularRvItemBinding

class PopularAdapter(private val recipes: List<Recipe>, private val context: Context) :
    RecyclerView.Adapter<PopularAdapter.PopularViewHolder>() {

    inner class PopularViewHolder(val binding: PopularRvItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(recipe: Recipe) {
            binding.recipeTitle.text = recipe.title
            binding.popularTime.text = recipe.cookingTime
            Glide.with(context).load(recipe.coverImage).into(binding.recipeImage)
            binding.root.setOnClickListener {
                var intent = Intent(context, RecipeActivity::class.java)
                intent.putExtra("img", recipe.coverImage)  // Recipe title
                intent.putExtra("title", recipe.title)  // Recipe title
                intent.putExtra("time", recipe.cookingTime)  // Recipe title
                intent.putExtra("popular_time", recipe.cookingTime)  // Recipe title
                intent.putExtra("ing", recipe.description)  // Recipe title
                intent.putExtra("des", recipe.description)  // Meal type
                intent.putExtra("calories", recipe.calories.toInt())  // Calories
                intent.putExtra("fats", recipe.fats.toInt())  // Fats
                intent.putExtra("proteins", recipe.proteins.toInt())  // Fats
                intent.putExtra("carbs", recipe.carbs.toInt())  // Carbs
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                context.startActivity(intent)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PopularViewHolder {
        val binding = PopularRvItemBinding.inflate(from(context), parent, false)
        return PopularViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PopularViewHolder, position: Int) {
        holder.bind(recipes[position])
    }

    override fun getItemCount() = recipes.size
}
