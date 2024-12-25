package com.example.caloriesapp.fragments

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.example.caloriesapp.R
import com.example.caloriesapp.models.Meal
import com.example.caloriesapp.network.RetrofitInstance
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.*

class AddMealDialogFragment : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(requireContext())
        val inflater = requireActivity().layoutInflater
        val view = inflater.inflate(R.layout.dialog_add_meal, null)

        val titleInput = view.findViewById<EditText>(R.id.et_meal_title)
        val fatsInput = view.findViewById<EditText>(R.id.et_fats)
        val proteinsInput = view.findViewById<EditText>(R.id.et_proteins)
        val caloriesInput = view.findViewById<EditText>(R.id.et_calories)
        val carbsInput = view.findViewById<EditText>(R.id.et_carbs)
        val weightInput = view.findViewById<EditText>(R.id.et_weight)
        val mealTypeSpinner = view.findViewById<Spinner>(R.id.spinner_meal_type)


        builder.setView(view)
            .setPositiveButton("Save") { _, _ ->
                val title = titleInput.text.toString()
                val fats = fatsInput.text.toString().toIntOrNull()
                val proteins = proteinsInput.text.toString().toIntOrNull()
                val calories = caloriesInput.text.toString().toIntOrNull()
                val carbs = carbsInput.text.toString().toIntOrNull()
                val weight = weightInput.text.toString().toFloatOrNull()
                val mealType = mealTypeSpinner.selectedItem.toString()

                if (title.isNotEmpty() && fats != null && proteins != null && calories != null && carbs != null && weight != null) {
                    val newMeal = Meal(
                        title = title,
                        fats = fats,
                        proteins = proteins,
                        calories = calories,
                        carbs = carbs,
                        weight = weight,
                        mealType = mealType,
                        date = GregorianCalendar(2014, Calendar.FEBRUARY, 11).time
                    )
                    saveMeal(newMeal)
                } else {
                    Toast.makeText(context, "Please fill out all fields", Toast.LENGTH_SHORT).show()
                }
            }
            .setNegativeButton("Cancel") { dialog, _ ->
                dialog.dismiss()
            }

        return builder.create()
    }

    private fun saveMeal(meal: Meal) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                println(meal)
                val response = RetrofitInstance.baseApi.createMeal(meal)
                withContext(Dispatchers.Main) {
                    if (isAdded) { // Check if the fragment is still attached
                        if (response.isSuccessful) {
                            Toast.makeText(
                                requireContext(),
                                "Meal saved successfully!",
                                Toast.LENGTH_SHORT
                            ).show()
                            (parentFragment as? DiaryFragment)?.fetchMeals() // Notify parent fragment
                            dismiss()
                        } else {
                            Toast.makeText(
                                requireContext(),
                                "Failed to save meal: ${response.message()}",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    if (isAdded) { // Ensure the fragment is attached
                        Toast.makeText(
                            requireContext(),
                            "Error saving meal: ${e.localizedMessage}",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        }

}
}
