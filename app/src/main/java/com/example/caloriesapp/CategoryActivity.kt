package com.example.caloriesapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.caloriesapp.databinding.ActivityCategoryBinding

class CategoryActivity : AppCompatActivity() {

    private lateinit var rvAdapter: CategoryAdapter
    private lateinit var dataList: ArrayList<Recipe>
    private val binding by lazy {
        ActivityCategoryBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        title = intent.getStringExtra("TITTLE")
        setupRecyclerView()
        binding.goBackHome.setOnClickListener {
            finish()
        }

    }

    private fun setupRecyclerView() {

        dataList = ArrayList()
        binding.rvCategory.layoutManager =
            LinearLayoutManager(this)

        //TODO call python recipe api
//        var recipes = emptyList<>()
//
//        for (i in recipes!!.indices) {
//            if (recipes[i]!!.category.contains(intent.getStringExtra("CATEGORY")!!)) {
//                dataList.add(recipes[i]!!)
//            }
//            rvAdapter = CategoryAdapter(dataList, this)
//            binding.rvCategory.adapter = rvAdapter
//        }
    }

}