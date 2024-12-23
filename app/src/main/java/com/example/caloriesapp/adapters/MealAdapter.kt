package com.example.caloriesapp.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.caloriesapp.databinding.ItemMealBinding
import com.example.caloriesapp.models.Meal
import java.text.SimpleDateFormat
import java.util.Locale

class MealAdapter(private var meals: List<Meal>) : RecyclerView.Adapter<MealAdapter.MealViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MealViewHolder {
        val binding = ItemMealBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MealViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MealViewHolder, position: Int) {
        val meal = meals[position]
        holder.bind(meal)
    }

    override fun getItemCount(): Int = meals.size

    fun updateMeals(newMeals: List<Meal>) {
        meals = newMeals
        notifyDataSetChanged()
    }

    inner class MealViewHolder(private val binding: ItemMealBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(meal: Meal) {
            binding.titleTextView.text = meal.title
            binding.fatsTextView.text = "${meal.fats}g"
            binding.proteinsTextView.text = "${meal.proteins}g"
            binding.caloriesTextView.text = "${meal.calories} kcal"
            binding.carbsTextView.text = "${meal.carbs}g"
            binding.mealTypeTextView.text = meal.mealType
            binding.weightTextView.text = "${meal.weight}g"
            binding.dateTextView.text = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(meal.date)
        }
    }
}
