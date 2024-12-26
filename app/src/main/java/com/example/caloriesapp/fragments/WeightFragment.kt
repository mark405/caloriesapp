package com.example.caloriesapp.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.caloriesapp.R
import com.example.caloriesapp.activities.UserOptionsActivity

class WeightFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.framnet_weight, container, false)

        val weightInput: EditText = view.findViewById(R.id.weightInput)
        val nextButton: Button = view.findViewById(R.id.nextWeightButton)

        nextButton.setOnClickListener {
            val weight = weightInput.text.toString()
            if (weight.isNotEmpty()) {
                val parentActivity = activity as? UserOptionsActivity
                parentActivity?.userWeight = weight
                // Navigate to com.example.caloriesapp.fragments.AgeFragment
                parentActivity?.viewPager?.currentItem = 4
            } else {
                Toast.makeText(requireContext(), "Please enter your weight", Toast.LENGTH_SHORT).show()
            }
        }
        return view
    }
}
