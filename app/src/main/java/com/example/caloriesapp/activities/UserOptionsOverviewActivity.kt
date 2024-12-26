package com.example.caloriesapp.activities

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.caloriesapp.databinding.ActivityOptionsOverviewBinding
import com.example.caloriesapp.fragments.EditFieldBottomSheet
import com.example.caloriesapp.fragments.EditSelectionBottomSheet
import com.example.caloriesapp.network.RetrofitInstance
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class UserOptionsOverviewActivity : AppCompatActivity() {

    private lateinit var binding: ActivityOptionsOverviewBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityOptionsOverviewBinding.inflate(layoutInflater)
        setContentView(binding.root)
        RetrofitInstance.init(applicationContext)

        fetchAndSetUserOptions()

        // Set click listeners for fields
        binding.tvGoal.setOnClickListener {
            val options = listOf("Втратити вагу", "Підтримувати нинішню вагу", "Набрати вагу")
            val currentValue = binding.tvGoal.text.toString()

            val dialog = EditSelectionBottomSheet("Goal", currentValue, options) { selectedValue ->
                binding.tvGoal.text = selectedValue // Update the TextView
            }
            dialog.show(supportFragmentManager, "EditGoalDialog")
        }

        binding.tvAge.setOnClickListener {
            openEditFieldDialog("Age", binding.tvAge.text.toString().replace(" years", "")) { newValue ->
                binding.tvAge.text = "$newValue years"
            }
        }

        binding.tvHeight.setOnClickListener {
            openEditFieldDialog("Height", binding.tvHeight.text.toString().replace(" cm", "")) { newValue ->
                binding.tvHeight.text = "$newValue cm"
            }
        }

        binding.tvWeight.setOnClickListener {
            openEditFieldDialog("Weight", binding.tvWeight.text.toString().replace(" kg", "")) { newValue ->
                binding.tvWeight.text = "$newValue kg"
            }
        }

        binding.tvGender.setOnClickListener {
            val options = listOf("Чоловік", "Жінка")
            val currentValue = binding.tvGender.text.toString()

            val dialog = EditSelectionBottomSheet("Gender", currentValue, options) { selectedValue ->
                binding.tvGender.text = selectedValue // Update the TextView
            }
            dialog.show(supportFragmentManager, "EditGenderDialog")
        }

        binding.tvLifestyle.setOnClickListener {
            val options = listOf("sedentary", "low_active", "active", "very_active")
            val currentValue = binding.tvLifestyle.text.toString()

            val dialog = EditSelectionBottomSheet("Lifestyle", currentValue, options) { selectedValue ->
                binding.tvLifestyle.text = selectedValue // Update the TextView
            }
            dialog.show(supportFragmentManager, "EditLifestyleDialog")
        }

        binding.btnSave.setOnClickListener {
            saveUpdatedUserOptions()
        }
    }

    private fun openEditFieldDialog(fieldName: String, currentValue: String, onSave: (String) -> Unit) {
        val dialog = EditFieldBottomSheet(fieldName, currentValue, onSave)
        dialog.show(supportFragmentManager, "EditFieldDialog")
    }

    private fun fetchAndSetUserOptions() {
        lifecycleScope.launch(Dispatchers.IO) {
            try {
                val response = RetrofitInstance.baseApi.getUserDetails()

                if (response.isSuccessful) {
                    val userOptions = response.body()

                    withContext(Dispatchers.Main) {
                        userOptions?.let {
                            // Populate fields with user options
                            binding.tvGoal.text = it.weightGoal
                            binding.tvAge.text = "${it.age} years"
                            binding.tvHeight.text = "${it.height} cm"
                            binding.tvWeight.text = "${it.weight} kg"
                            binding.tvGender.text = it.gender
                            binding.tvLifestyle.text = it.activityLevel
                        }
                    }
                } else {
                    withContext(Dispatchers.Main) {
                        Toast.makeText(
                            this@UserOptionsOverviewActivity,
                            "Failed to fetch user options: ${response.message()}",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(
                        this@UserOptionsOverviewActivity,
                        "Error fetching user options: ${e.localizedMessage}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    private fun saveUpdatedUserOptions() {
        // Collect updated values
        val gender = binding.tvGender.text.toString()
        val height = binding.tvHeight.text.toString().replace(" cm", "").toFloat() // Use Float here
        val weight = binding.tvWeight.text.toString().replace(" kg", "").toFloat()
        val weightGoal = binding.tvGoal.text.toString()
        val activityLevel = binding.tvLifestyle.text.toString()
        val age = binding.tvAge.text.toString().replace(" years", "").toInt()

        lifecycleScope.launch(Dispatchers.IO) {
            try {
                // Create the updated user options object
                val updatedOptions = mapOf(
                    "gender" to gender,
                    "height" to height.toString(),
                    "weight" to weight.toString(),
                    "weightGoal" to weightGoal,
                    "activityLevel" to activityLevel,
                    "age" to age.toString()
                )
                println(updatedOptions)
                // Make the API call to update user options
                val response = RetrofitInstance.baseApi.updateUserOptions(updatedOptions)

                // Check response
                withContext(Dispatchers.Main) {
                    if (response.isSuccessful) {
                        Toast.makeText(
                            this@UserOptionsOverviewActivity,
                            "User options updated!",
                            Toast.LENGTH_SHORT
                        ).show()
                        finish() // Close the activity after successful save
                    } else {
                        Toast.makeText(
                            this@UserOptionsOverviewActivity,
                            "Failed to update options: ${response.message()}",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(
                        this@UserOptionsOverviewActivity,
                        "Error: ${e.localizedMessage}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }
}
