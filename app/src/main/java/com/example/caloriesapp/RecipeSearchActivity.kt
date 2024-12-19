package com.example.caloriesapp

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.caloriesapp.databinding.ActivitySearchBinding

class RecipeSearchActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySearchBinding
    private lateinit var rvAdapter: RecipeSearchAdapter
    private lateinit var dataList: ArrayList<Recipe>
    private lateinit var recipes: List<Recipe?>

    @SuppressLint("ServiceCast")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.search.requestFocus()

        //TODO python api call
//        recipes = emptyList()
        setupRecyclerView()
        binding.goBackHome.setOnClickListener {
            finish()
        }
        binding.search.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s.toString() != "") {
                    filterData(s.toString())
                } else {
                    setupRecyclerView()
                }
            }

            override fun afterTextChanged(s: Editable?) {
            }

        })

    }

    private fun filterData(filterText: String) {
        var filterData = ArrayList<Recipe>()
        for (i in recipes.indices) {
            if (recipes[i]!!.tittle.lowercase().contains(filterText.lowercase())) {
                filterData.add(recipes[i]!!)
            }
            rvAdapter.filerList(filterList = filterData)
        }
    }

    private fun setupRecyclerView() {

        dataList = ArrayList()
        binding.rvSearch.layoutManager =
            LinearLayoutManager(this)

        for (i in recipes!!.indices) {
            if (recipes[i]!!.category.contains("Popular")) {
                dataList.add(recipes[i]!!)
            }
            rvAdapter = RecipeSearchAdapter(dataList, this)
            binding.rvSearch.adapter = rvAdapter
        }
    }
}