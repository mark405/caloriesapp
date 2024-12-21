package com.example.caloriesapp

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.caloriesapp.databinding.ActivityMainBinding
import com.example.caloriesapp.databinding.ActivityReceiptBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var bindingReceipt: ActivityReceiptBinding
    private lateinit var rvAdapter: PopularAdapter
    private lateinit var dataList: ArrayList<Recipe>


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (!isUserLoggedIn()) {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
            return
        }

        binding = ActivityMainBinding.inflate(layoutInflater)
        bindingReceipt = ActivityReceiptBinding.inflate(layoutInflater)

        setContentView(binding.root)

        setupRecyclerView()

        bindingReceipt.search.setOnClickListener {
            startActivity(Intent(this, RecipeSearchActivity::class.java))
        }

        bindingReceipt.salad.setOnClickListener {
            var intent = Intent(this@MainActivity, CategoryActivity::class.java)
            intent.putExtra("TITTLE", "Salad")
            intent.putExtra("CATEGORY", "Salad")
            startActivity(intent)
        }
        bindingReceipt.mainDish.setOnClickListener {
            var intent = Intent(this@MainActivity, CategoryActivity::class.java)
            intent.putExtra("TITTLE", "Main Dish")
            intent.putExtra("CATEGORY", "Dish")
            startActivity(intent)
        }
        bindingReceipt.drinks.setOnClickListener {
            var intent = Intent(this@MainActivity, CategoryActivity::class.java)
            intent.putExtra("TITTLE", "Drinks")
            intent.putExtra("CATEGORY", "Drinks")
            startActivity(intent)
        }
        bindingReceipt.desserts.setOnClickListener {
            var intent = Intent(this@MainActivity, CategoryActivity::class.java)
            intent.putExtra("TITTLE", "Desserts")
            intent.putExtra("CATEGORY", "Desserts")
            startActivity(intent)
        }

        binding.logoutButton.setOnClickListener {
            logout()
        }
    }

    private fun isUserLoggedIn(): Boolean {
        val sharedPreferences: SharedPreferences = getSharedPreferences("AppPrefs", MODE_PRIVATE)
        val token = sharedPreferences.getString("accessToken", null)
        return !token.isNullOrEmpty()
    }
    private fun logout() {
        // Clear the stored token
        val sharedPreferences: SharedPreferences = getSharedPreferences("AppPrefs", MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.remove("accessToken")  // Remove the access token
        editor.apply()

        // Redirect to LoginActivity
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finish()  // Finish current activity to remove it from back stack
    }

    private fun setupRecyclerView() {

        dataList = ArrayList()
        bindingReceipt.rvPopular.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)

        //TODO call python recipes api
//        var recipes = emptyList()
//
//        for (i in recipes!!.indices) {
//            if (recipes[i]!!.category.contains("Popular")) {
//                dataList.add(recipes[i]!!)
//            }
//            rvAdapter = PopularAdapter(dataList, this)
//            bindingReceipt.rvPopular.adapter = rvAdapter
//        }
    }

}
