package com.example.caloriesapp

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.caloriesapp.databinding.PopularRvItemBinding

class PopularAdapter(
    var dataList: ArrayList<Recipe>,
    var context: Context
) : RecyclerView.Adapter<PopularAdapter.ViewHolder>() {

    inner class ViewHolder(var binding: PopularRvItemBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = PopularRvItemBinding.inflate(LayoutInflater.from(context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val recipe = dataList[position]
        println(dataList)
        Glide.with(context)
            .load(recipe.coverImage)
            .into(holder.binding.popularImg)

        // Set recipe title
        holder.binding.popularTxt.text = recipe.title

        // Split the cooking time string and display it
        val time = recipe.cookingTime
        holder.binding.popularTime.text = time ?: "N/A"

        // You can display nutritional information if desired (e.g., calories, fats, carbs)

        // Handle item click to show detailed recipe view
        holder.itemView.setOnClickListener {
            val intent = Intent(context, RecipeActivity::class.java)
            intent.putExtra("img", recipe.coverImage)  // Recipe title
            intent.putExtra("title", recipe.title)  // Recipe title
            intent.putExtra("des", recipe.mealType)  // Meal type
            intent.putExtra("calories", recipe.calories)  // Calories
            intent.putExtra("fats", recipe.fats)  // Fats
            intent.putExtra("carbs", recipe.carbs)  // Carbs
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            context.startActivity(intent)
        }
    }
}
