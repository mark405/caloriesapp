package com.example.caloriesapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment

class Step2Fragment : Fragment() {

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

        // Proceed to Step3Fragment
        nextButton.setOnClickListener {
                if (selectedActivity != null) {
                    (activity as UserOptionsActivity).selectedActivity = selectedActivity // Store the activity level
                    val parentActivity = activity as? UserOptionsActivity
                    parentActivity?.viewPager?.currentItem = 2 // Move to Step2Fragment
                } else {
                    Toast.makeText(requireContext(), "Виберіть активність", Toast.LENGTH_SHORT).show()
                }

        }
        return view
    }

    private fun highlightSelectedButton(selected: Button, vararg others: Button) {
        selected.setBackgroundColor(resources.getColor(R.color.green, null))
        others.forEach { it.setBackgroundColor(resources.getColor(android.R.color.darker_gray, null)) }
    }
}
