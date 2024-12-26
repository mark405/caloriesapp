package com.example.caloriesapp.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.caloriesapp.activities.CategoryActivity
import com.example.caloriesapp.activities.RecipeSearchActivity
import com.example.caloriesapp.adapters.PopularAdapter
import com.example.caloriesapp.databinding.FragmentRecipesBinding
import com.example.caloriesapp.models.Recipe
import com.example.caloriesapp.network.RetrofitInstance
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class RecipesFragment : Fragment() {

    private var _binding: FragmentRecipesBinding? = null
    private val binding get() = _binding!!

    private lateinit var popularAdapter: PopularAdapter
    private lateinit var allRecipesAdapter: PopularAdapter
    private lateinit var popularRecipesList: ArrayList<Recipe>
    private lateinit var allRecipesList: ArrayList<Recipe>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRecipesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Set up RecyclerViews
        setupRecyclerViews()

        // Handle search button click
        binding.search.setOnClickListener {
            startActivity(Intent(requireContext(), RecipeSearchActivity::class.java))
        }

        // Handle category button clicks
        binding.salad.setOnClickListener {
            navigateToCategory("Сніданок", "Breakfast")
        }
        binding.mainDish.setOnClickListener {
            navigateToCategory("Обід", "Lunch")
        }
        binding.drinks.setOnClickListener {
            navigateToCategory("Вечеря", "Dinner")
        }
        binding.desserts.setOnClickListener {
            navigateToCategory("Снеки", "Snack")
        }

        // Fetch Recipes
        fetchRecipes()
    }

    private fun setupRecyclerViews() {
        // Setup Popular Recipes RecyclerView
        popularRecipesList = ArrayList()
        binding.rvPopular.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        popularAdapter = PopularAdapter(popularRecipesList, requireContext())
        binding.rvPopular.adapter = popularAdapter

        // Setup All Recipes RecyclerView for horizontal scrolling
        allRecipesList = ArrayList()
        binding.rvAllRecipes.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        allRecipesAdapter = PopularAdapter(allRecipesList, requireContext())
        binding.rvAllRecipes.adapter = allRecipesAdapter
    }

    private fun fetchRecipes() {
        CoroutineScope(Dispatchers.Main).launch {
            try {
                val response = RetrofitInstance.baseApi.getRecipes()
                if (response.isSuccessful) {
                    val recipes = response.body() ?: emptyList()

                    // Separate popular recipes
                    val popularRecipes = recipes.filter { it.isPopular }
                    popularRecipesList.clear()
                    popularRecipesList.addAll(popularRecipes)
                    popularAdapter.notifyDataSetChanged()

                    // All recipes (including popular ones)
                    allRecipesList.clear()
                    allRecipesList.addAll(recipes)
                    allRecipesAdapter.notifyDataSetChanged()
                } else {
                    Toast.makeText(
                        requireContext(),
                        "Failed to load recipes: ${response.message()}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            } catch (e: Exception) {
                Toast.makeText(requireContext(), "Error occurred: ${e.localizedMessage}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun navigateToCategory(title: String, category: String) {
        val intent = Intent(requireContext(), CategoryActivity::class.java).apply {
            putExtra("TITLE", title)
            putExtra("CATEGORY", category)
        }
        startActivity(intent)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
