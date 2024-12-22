package com.example.caloriesapp

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.caloriesapp.databinding.SearchRvBinding

class RecipeSearchAdapter(var dataList: ArrayList<Recipe>, var context: Context) :
    RecyclerView.Adapter<RecipeSearchAdapter.ViewHolder>() {

    inner class ViewHolder(var binding: SearchRvBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = SearchRvBinding.inflate(LayoutInflater.from(context), parent, false)
        return ViewHolder(view)
    }

    @SuppressLint("NotifyDataSetChanged")
    fun filterList(filterList: ArrayList<Recipe>) {
        dataList = filterList
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        // Load image using Glide. You might need to adapt this if `mealType` doesn't represent an image URL.
        Glide.with(context)
            .load(dataList[position].coverImage) // Assuming mealType holds an image URL or identifier.
            .into(holder.binding.searchImg)

        // Set recipe title to the TextView
        holder.binding.searchTxt.text = dataList[position].title

        // Set an onClickListener for item clicks
        holder.itemView.setOnClickListener {
            val intent = Intent(context, RecipeActivity::class.java)

            // Pass the relevant data fields to the new activity
            intent.putExtra("img", dataList[position].coverImage)  // Recipe title
            intent.putExtra("title", dataList[position].title)  // Recipe title
            intent.putExtra("ing", TextUtils.join(",", dataList[position].ingredients))  // Recipe title
            intent.putExtra("des", dataList[position].cookingTime)  // Meal type
            intent.putExtra("calories", dataList[position].calories)  // Calories
            intent.putExtra("fats", dataList[position].fats)  // Fats
            intent.putExtra("carbs", dataList[position].carbs)  // Carbs

            // Set flag to ensure new activity starts in a clean task
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            context.startActivity(intent)
        }
    }
}
