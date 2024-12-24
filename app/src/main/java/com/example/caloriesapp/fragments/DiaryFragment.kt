package com.example.caloriesapp.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.caloriesapp.R
import com.example.caloriesapp.adapters.MealAdapter
import com.example.caloriesapp.network.RetrofitInstance
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class DiaryFragment : Fragment() {

    private lateinit var mealsRecyclerView: RecyclerView
    private lateinit var mealAdapter: MealAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_diary, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mealsRecyclerView = view.findViewById(R.id.meals_recycler_view)
        mealAdapter = MealAdapter(emptyList())
        mealsRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        mealsRecyclerView.adapter = mealAdapter

        fetchMeals()
    }

    private fun fetchMeals() {
        CoroutineScope(Dispatchers.IO).launch {
            val response = RetrofitInstance.baseApi.getMeals()
            withContext(Dispatchers.Main) {
                if (response.isSuccessful) {
                    response.body()?.let { meals ->
                        mealAdapter.updateMeals(meals)
                    }
                } else {
                    Toast.makeText(requireContext(), "Error fetching meals", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}
