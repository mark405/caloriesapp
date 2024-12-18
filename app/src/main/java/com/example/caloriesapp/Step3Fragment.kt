package com.example.caloriesapp

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.NumberPicker
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.caloriesapp.network.RetrofitInstance
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class Step3Fragment : Fragment() {

    private var heightMeters = 1
    private var heightCm = 70

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_step3, container, false)

        val heightMeterPicker: NumberPicker = view.findViewById(R.id.heightMeterPicker)
        val heightCmPicker: NumberPicker = view.findViewById(R.id.heightCmPicker)
        val finishButton: Button = view.findViewById(R.id.finishButton)

        // Initialize pickers
        heightMeterPicker.minValue = 1
        heightMeterPicker.maxValue = 2
        heightMeterPicker.value = heightMeters

        heightCmPicker.minValue = 0
        heightCmPicker.maxValue = 99
        heightCmPicker.value = heightCm

        finishButton.setOnClickListener {
            val parentActivity = activity as? UserOptionsActivity
            val goal = parentActivity?.selectedGoal ?: "Not Set"
            val activityLevel = parentActivity?.selectedActivity ?: "Not Set"
            val height = "${heightMeterPicker.value}.${heightCmPicker.value}"

            // Print collected data
            println("Goal: $goal, Activity Level: $activityLevel, Height: $height")

            // Send data to the server
            sendDataToServer(goal, activityLevel, height)
        }

        return view
    }

    private fun sendDataToServer(goal: String, activityLevel: String, height: String) {
        // Use Retrofit to send data
        val apiService = RetrofitInstance.api
        val userData = mapOf(
            "goal" to goal,
            "activityLevel" to activityLevel,
            "height" to height
        )

        // Make the HTTP request in a coroutine
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = apiService.saveUserOptions(userData)
                withContext(Dispatchers.Main) {
                    if (response.isSuccessful) {
                        Toast.makeText(requireContext(), "Data saved successfully!", Toast.LENGTH_SHORT).show()

                        // Navigate to MainActivity
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
        val intent = Intent(requireContext(), MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
    }
}
