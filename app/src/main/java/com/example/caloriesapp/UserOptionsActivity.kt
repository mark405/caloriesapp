package com.example.caloriesapp

import UserOptionsAdapter
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2

class UserOptionsActivity : AppCompatActivity() {

    lateinit var viewPager: ViewPager2

    // Shared variables for user data
    var selectedGoal: String? = null
    var selectedActivity: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_options)

        viewPager = findViewById(R.id.viewPager)
        val adapter = UserOptionsAdapter(this)
        viewPager.adapter = adapter
    }
}
