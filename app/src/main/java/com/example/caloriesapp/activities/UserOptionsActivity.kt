package com.example.caloriesapp.activities

import com.example.caloriesapp.adapters.UserOptionsAdapter
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.example.caloriesapp.R

class UserOptionsActivity : AppCompatActivity() {

    lateinit var viewPager: ViewPager2

    // Shared variables for user data
    var selectedGoal: String? = null
    var selectedActivity: String? = null
    var userAge: String? = null
    var userWeight: String? = null
    var userHeight: String? = null
    var userGender: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_options)

        viewPager = findViewById(R.id.viewPager)
        val adapter = UserOptionsAdapter(this)
        viewPager.adapter = adapter
    }
}
