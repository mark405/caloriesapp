package com.example.caloriesapp.fragments

import android.content.Context.MODE_PRIVATE
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.caloriesapp.network.RetrofitInstance
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import com.example.caloriesapp.activities.MainActivity
import com.example.caloriesapp.R
import com.example.caloriesapp.activities.DiaryActivity
import com.example.caloriesapp.activities.UserOptionsActivity

class ActivityFragment : Fragment() {

    private var selectedActivity: String? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_step2, container, false)

        // Initialize buttons
        val inactiveButton: Button = view.findViewById(R.id.inactiveButton)
        val lowActivityButton: Button = view.findViewById(R.id.lowActivityButton)
        val activeButton: Button = view.findViewById(R.id.activeButton)
        val veryActiveButton: Button = view.findViewById(R.id.veryActiveButton)
        val nextButton: Button = view.findViewById(R.id.nextButton)

        // Button click listeners
        inactiveButton.setOnClickListener {
            selectedActivity = "Неактивний"
            highlightSelectedButton(inactiveButton, lowActivityButton, activeButton, veryActiveButton)
        }

        lowActivityButton.setOnClickListener {
            selectedActivity = "Низька активність"
            highlightSelectedButton(lowActivityButton, inactiveButton, activeButton, veryActiveButton)
        }

        activeButton.setOnClickListener {
            selectedActivity = "Активний"
            highlightSelectedButton(activeButton, inactiveButton, lowActivityButton, veryActiveButton)
        }

        veryActiveButton.setOnClickListener {
            selectedActivity = "Дуже активний"
            highlightSelectedButton(veryActiveButton, inactiveButton, lowActivityButton, activeButton)
        }

        nextButton.setOnClickListener {
            val parentActivity = activity as? UserOptionsActivity
            val goal = parentActivity?.selectedGoal ?: "Not Set"
            val activityLevel = selectedActivity ?: "Not Set"
            val height = parentActivity?.userHeight ?: "Not Set"
            val weight = parentActivity?.userWeight ?: "Not Set"
            val age = parentActivity?.userAge ?: "Not Set"
            val gender = parentActivity?.userGender ?: "Not Set"

            sendDataToServer(goal, activityLevel, height, weight, age, gender)
        }

        return view
    }

    private fun sendDataToServer(weightGoal: String, activityLevel: String, height: String, weight: String, age: String, gender: String) {
        val apiService = RetrofitInstance.baseApi
        val userData = mapOf(
            "weightGoal" to weightGoal,
            "activityLevel" to activityLevel,
            "height" to height,
            "age" to age,
            "gender" to gender,
            "weight" to weight
        )

        println(userData)

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = apiService.saveUserOptions(userData)
                withContext(Dispatchers.Main) {
                    if (response.isSuccessful) {
                        Toast.makeText(requireContext(), "Data saved successfully!", Toast.LENGTH_SHORT).show()
                        saveUserOptions("userOptions", true)
                        val intent = Intent(requireContext(), MainActivity::class.java)
                        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                        startActivity(intent)
                    } else {
                        println("Failed to save data: ${response.message()}")
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(requireContext(), "Error: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            }
        }
        val intent = Intent(requireContext(), DiaryActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
    }

    private fun highlightSelectedButton(selected: Button, vararg others: Button) {
        selected.setBackgroundColor(resources.getColor(R.color.green, null))
        others.forEach { it.setBackgroundColor(resources.getColor(android.R.color.darker_gray, null)) }
    }

    private fun saveUserOptions(key: String, value: Boolean) {
        val sharedPreferences: SharedPreferences = requireContext().getSharedPreferences("AppPrefs", MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putBoolean(key, value)
        editor.apply()
    }
}
