package com.example.caloriesapp.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.caloriesapp.models.Recipe
import com.example.caloriesapp.activities.RecipeActivity
import com.example.caloriesapp.databinding.CategoryRvBinding

class CategoryAdapter(var dataList: ArrayList<Recipe>, var context: Context) :
    RecyclerView.Adapter<CategoryAdapter.ViewHolder>() {

    inner class ViewHolder(var binding: CategoryRvBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var binding = CategoryRvBinding.inflate(LayoutInflater.from(context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        Glide.with(context).load(dataList[position].coverImage).into(holder.binding.img)
        holder.binding.tittle.text = dataList.get(position).title

        holder.binding.time.text = dataList[position].cookingTime

        holder.binding.next.setOnClickListener {
            var intent = Intent(context, RecipeActivity::class.java)
            intent.putExtra("img", dataList[position].coverImage)  // Recipe title
            intent.putExtra("title", dataList[position].title)  // Recipe title
            intent.putExtra("time", dataList[position].cookingTime)  // Recipe title
            intent.putExtra("ing", dataList[position].description)  // Recipe title
            intent.putExtra("des", dataList[position].description)  // Meal type
            intent.putExtra("calories", dataList[position].calories.toInt())  // Calories
            intent.putExtra("fats", dataList[position].fats.toInt())  // Fats
            intent.putExtra("proteins", dataList[position].proteins.toInt())  // Fats
            intent.putExtra("carbs", dataList[position].carbs.toInt())  // Carbs
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            context.startActivity(intent)
        }

    }

}