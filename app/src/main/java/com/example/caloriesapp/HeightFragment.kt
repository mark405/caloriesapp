package com.example.caloriesapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.NumberPicker
import android.widget.Toast
import androidx.fragment.app.Fragment

class HeightFragment : Fragment() {

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

        heightMeterPicker.minValue = 1
        heightMeterPicker.maxValue = 2
        heightMeterPicker.value = heightMeters

        heightCmPicker.minValue = 0
        heightCmPicker.maxValue = 99
        heightCmPicker.value = heightCm

        finishButton.setOnClickListener {
            val meters = heightMeterPicker.value
            val centimeters = heightCmPicker.value

            val height = "$meters.$centimeters"

            if (height.isNotEmpty()) {
                val parentActivity = activity as? UserOptionsActivity
                parentActivity?.userHeight = height

                Toast.makeText(requireContext(), "Height saved: $height meters", Toast.LENGTH_SHORT).show()

                parentActivity?.viewPager?.currentItem = 3
            } else {
                Toast.makeText(requireContext(), "Please enter your height", Toast.LENGTH_SHORT).show()
            }
        }

        return view
    }
}
