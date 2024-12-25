package com.example.caloriesapp.fragments

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.caloriesapp.R
import com.example.caloriesapp.activities.MainActivity
import com.example.caloriesapp.activities.UserOptionsActivity
import com.example.caloriesapp.network.RetrofitInstance
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject

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

        // Initialize Retrofit with context
        context?.let { RetrofitInstance.init(it) }

        // Button click listeners
        inactiveButton.setOnClickListener {
            selectedActivity = "sedentary"
            highlightSelectedButton(inactiveButton, lowActivityButton, activeButton, veryActiveButton)
        }

        lowActivityButton.setOnClickListener {
            selectedActivity = "low_active"
            highlightSelectedButton(lowActivityButton, inactiveButton, activeButton, veryActiveButton)
        }

        activeButton.setOnClickListener {
            selectedActivity = "active"
            highlightSelectedButton(activeButton, inactiveButton, lowActivityButton, veryActiveButton)
        }

        veryActiveButton.setOnClickListener {
            selectedActivity = "very_active"
            highlightSelectedButton(veryActiveButton, inactiveButton, lowActivityButton, activeButton)
        }

        nextButton.setOnClickListener {
            saveDataAndProceed()
        }

        return view
    }

    private fun saveDataAndProceed() {
        val parentActivity = activity as? UserOptionsActivity
        val goal = parentActivity?.selectedGoal ?: "Not Set"
        val activityLevel = selectedActivity ?: "Not Set"
        val height = parentActivity?.userHeight ?: "Not Set"
        val weight = parentActivity?.userWeight ?: "Not Set"
        val age = parentActivity?.userAge ?: "Not Set"
        val gender = parentActivity?.userGender ?: "Not Set"

        sendDataToServer(goal, activityLevel, height, weight, age, gender)
    }

    private fun sendDataToServer(
        weightGoal: String,
        activityLevel: String,
        height: String,
        weight: String,
        age: String,
        gender: String
    ) {
        val apiService = RetrofitInstance.baseApi
        val userData = mapOf(
            "weightGoal" to weightGoal,
            "activityLevel" to activityLevel,
            "height" to height,
            "age" to age,
            "gender" to gender,
            "weight" to weight,
        )

        lifecycleScope.launch(Dispatchers.IO) {
            try {
                val response = apiService.saveUserOptions(userData)
                withContext(Dispatchers.Main) {
                    if (response.isSuccessful) {
                        Toast.makeText(requireContext(), "Data saved successfully!", Toast.LENGTH_SHORT).show()
                        saveUserOptionsToLocalStorage(
                            weightGoal, activityLevel, height, weight, age, gender
                        )
                        navigateToMainActivity()
                    } else {
                        Toast.makeText(requireContext(), "Failed to save data: ${response.message()}", Toast.LENGTH_SHORT).show()
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(requireContext(), "Error: ${e.localizedMessage}", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun navigateToMainActivity() {
        val intent = Intent(requireContext(), MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
    }

    private fun saveUserOptionsToLocalStorage(
        weightGoal: String,
        activityLevel: String,
        height: String,
        weight: String,
        age: String,
        gender: String
    ) {
        val sharedPreferences: SharedPreferences = requireContext().getSharedPreferences("UserPrefs", android.content.Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()

        val userOptions = JSONObject().apply {
            put("weightGoal", weightGoal)
            put("activityLevel", activityLevel)
            put("height", height)
            put("weight", weight)
            put("age", age)
            put("gender", gender)
        }.toString()

        editor.putString("userOptions", userOptions)
        editor.apply()
    }

    private fun highlightSelectedButton(selected: Button, vararg others: Button) {
        selected.setBackgroundColor(resources.getColor(R.color.green, null))
        others.forEach { it.setBackgroundColor(resources.getColor(android.R.color.darker_gray, null)) }
    }
}
