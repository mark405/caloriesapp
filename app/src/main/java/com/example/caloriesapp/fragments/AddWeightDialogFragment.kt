import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.Dialog
import android.os.Bundle
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.example.caloriesapp.R
import com.example.caloriesapp.fragments.ReportsFragment
import com.example.caloriesapp.fragments.WeightEntry
import com.example.caloriesapp.network.RetrofitInstance
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class AddWeightDialogFragment : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(requireContext())
        val inflater = requireActivity().layoutInflater
        val view = inflater.inflate(R.layout.dialog_add_weight, null)

        val weightInput = view.findViewById<EditText>(R.id.et_weight)
        val dateInput = view.findViewById<TextView>(R.id.tv_date)

        // Set the default date to today's date
        val calendar = Calendar.getInstance()
        updateDateInView(calendar, dateInput)

        // Open DatePickerDialog on clicking the date input
        dateInput.setOnClickListener {
            DatePickerDialog(
                requireContext(),
                { _, year, month, dayOfMonth ->
                    calendar.set(year, month, dayOfMonth)
                    updateDateInView(calendar, dateInput)
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
            ).show()
        }

        builder.setView(view)
            .setPositiveButton("Save") { _, _ ->
                val weight = weightInput.text.toString().toDoubleOrNull()
                val date = dateInput.text.toString()

                if (weight != null && date.isNotEmpty()) {
                    // Call the API to add the weight entry
                    addWeightToServer(weight, date)
                } else {
                    Toast.makeText(context, "Invalid input", Toast.LENGTH_SHORT).show()
                }
            }
            .setNegativeButton("Cancel") { dialog, _ ->
                dialog.cancel()
            }

        return builder.create()
    }

    private fun updateDateInView(calendar: Calendar, dateInput: TextView) {
        val format = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        dateInput.text = format.format(calendar.time)
    }

    private fun addWeightToServer(weight: Double, date: String) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = RetrofitInstance.baseApi.addWeight(
                   WeightEntry(date, weight)
                )
                withContext(Dispatchers.Main) {
                    if (isAdded) {
                        if (response.isSuccessful) {
                            Toast.makeText(
                                requireContext(),
                                "Weight entry added successfully!",
                                Toast.LENGTH_SHORT
                            ).show()
                            (parentFragment as? ReportsFragment)?.refreshData()
                            dismiss()
                        } else {
                            Toast.makeText(
                                requireContext(),
                                "Failed to add weight entry: ${response.message()}",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    if (isAdded) {
                        Toast.makeText(
                            requireContext(),
                            "Error: ${e.localizedMessage}",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        }
    }
}
