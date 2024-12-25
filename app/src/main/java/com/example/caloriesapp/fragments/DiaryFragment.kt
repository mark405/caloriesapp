package com.example.caloriesapp.fragments

import MealAdapter
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.caloriesapp.R
import com.example.caloriesapp.activities.MealActivity
import com.example.caloriesapp.models.Meal
import com.example.caloriesapp.models.UserMacros
import com.example.caloriesapp.network.RetrofitInstance
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class DiaryFragment : Fragment() {

    private lateinit var mealsRecyclerView: RecyclerView
    private lateinit var mealAdapter: MealAdapter

    private lateinit var proteinsProgressBar: ProgressBar
    private lateinit var fatsProgressBar: ProgressBar
    private lateinit var carbsProgressBar: ProgressBar
    private lateinit var caloriesProgressBar: ProgressBar

    private lateinit var proteinsTextView: TextView
    private lateinit var fatsTextView: TextView
    private lateinit var carbsTextView: TextView
    private lateinit var caloriesTextView: TextView

    private lateinit var addMealButton: Button

    private var userMacros: UserMacros? = null
    private var totalMealMacros: UserMacros = UserMacros(0.0, 0.0, 0.0, 0.0)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_diary, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        context?.let { RetrofitInstance.init(it) }

        // Initialize RecyclerView
        mealsRecyclerView = view.findViewById(R.id.meals_recycler_view)
        mealAdapter = MealAdapter(emptyList()) { meal ->
            val intent = Intent(requireContext(), MealActivity::class.java).apply {
                putExtra("mealTitle", meal.title)
                putExtra("mealType", meal.mealType)
                putExtra("mealCalories", meal.calories)
                putExtra("mealProteins", meal.proteins)
                putExtra("mealFats", meal.fats)
                putExtra("mealCarbs", meal.carbs)
                putExtra("mealWeight", meal.weight)
                putExtra("mealDate", meal.date.toString())
            }
            startActivity(intent)
        }
        mealsRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        mealsRecyclerView.adapter = mealAdapter
        mealsRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        mealsRecyclerView.adapter = mealAdapter

        // Initialize progress bars and text views
        proteinsProgressBar = view.findViewById(R.id.proteins_progress_bar)
        fatsProgressBar = view.findViewById(R.id.fats_progress_bar)
        carbsProgressBar = view.findViewById(R.id.carbs_progress_bar)
        caloriesProgressBar = view.findViewById(R.id.calories_progress_bar)

        proteinsTextView = view.findViewById(R.id.proteins_text_view)
        fatsTextView = view.findViewById(R.id.fats_text_view)
        carbsTextView = view.findViewById(R.id.carbs_text_view)
        caloriesTextView = view.findViewById(R.id.calories_text_view)

        // Initialize Add Meal button
        addMealButton = view.findViewById(R.id.btn_add_meal)
        addMealButton.setOnClickListener {
            openAddMealDialog()
        }



        fetchMeals()
        fetchUserMacros()
    }

    fun fetchMeals() {
        CoroutineScope(Dispatchers.IO).launch {
            val response = RetrofitInstance.baseApi.getMeals()
            withContext(Dispatchers.Main) {
                if (response.isSuccessful) {
                    response.body()?.let { meals ->
                        mealAdapter.updateMeals(meals)
                        calculateTotalMealMacros(meals)
                        updateMacrosUI()
                    }
                } else {
                    Toast.makeText(requireContext(), "Error fetching meals", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun fetchUserMacros() {
        CoroutineScope(Dispatchers.IO).launch {
            val response = RetrofitInstance.baseApi.getUserMacros()
            withContext(Dispatchers.Main) {
                if (response.isSuccessful) {
                    response.body()?.let { macros ->
                        userMacros = macros
                        updateMacrosUI()
                    }
                } else {
                    Toast.makeText(requireContext(), "Error fetching macros", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun calculateTotalMealMacros(meals: List<Meal>) {
        totalMealMacros = meals.fold(UserMacros(0.0, 0.0, 0.0, 0.0)) { acc, meal ->
            acc.apply {
                proteins += meal.proteins
                fats += meal.fats
                carbs += meal.carbs
                calories += meal.calories
            }
        }
    }

    private fun updateMacrosUI() {
        val macros = userMacros ?: return

        // Update protein
        val proteinsProgress = (totalMealMacros.proteins / macros.proteins * 100).toInt()
        proteinsProgressBar.progress = proteinsProgress
        proteinsTextView.text = "${totalMealMacros.proteins.toInt()} / ${macros.proteins.toInt()} Proteins"

        // Update fats
        val fatsProgress = (totalMealMacros.fats / macros.fats * 100).toInt()
        fatsProgressBar.progress = fatsProgress
        fatsTextView.text = "${totalMealMacros.fats.toInt()} / ${macros.fats.toInt()} Fats"

        // Update carbs
        val carbsProgress = (totalMealMacros.carbs / macros.carbs * 100).toInt()
        carbsProgressBar.progress = carbsProgress
        carbsTextView.text = "${totalMealMacros.carbs.toInt()} / ${macros.carbs.toInt()} Carbs"

        // Update calories
        val caloriesProgress = (totalMealMacros.calories / macros.calories * 100).toInt()
        caloriesProgressBar.progress = caloriesProgress
        caloriesTextView.text = "${totalMealMacros.calories.toInt()} / ${macros.calories.toInt()} Calories"
    }

    private fun openAddMealDialog() {
        val addMealDialog = AddMealDialogFragment()
        addMealDialog.show(parentFragmentManager, "AddMealDialog")
    }
}
