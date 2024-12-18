package com.example.caloriesapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment

class Step1Fragment : Fragment() {

    private var selectedGoal: String? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_step1, container, false)

        val loseWeightButton: Button = view.findViewById(R.id.loseWeightButton)
        val maintainWeightButton: Button = view.findViewById(R.id.maintainWeightButton)
        val gainWeightButton: Button = view.findViewById(R.id.gainWeightButton)
        val nextButton: Button = view.findViewById(R.id.nextButton)

        loseWeightButton.setOnClickListener {
            selectedGoal = "Втратити вагу"
            highlightSelectedButton(loseWeightButton, maintainWeightButton, gainWeightButton)
        }

        maintainWeightButton.setOnClickListener {
            selectedGoal = "Підтримувати нинішню вагу"
            highlightSelectedButton(maintainWeightButton, loseWeightButton, gainWeightButton)
        }

        gainWeightButton.setOnClickListener {
            selectedGoal = "Набрати вагу"
            highlightSelectedButton(gainWeightButton, loseWeightButton, maintainWeightButton)
        }

        nextButton.setOnClickListener {
            if (selectedGoal != null) {
                (activity as UserOptionsActivity).selectedGoal = selectedGoal // Store the goal
                val parentActivity = activity as? UserOptionsActivity
                parentActivity?.viewPager?.currentItem = 1 // Move to Step2Fragment
            } else {
                Toast.makeText(requireContext(), "Виберіть ціль", Toast.LENGTH_SHORT).show()
            }
        }

        return view
    }

    private fun highlightSelectedButton(selected: Button, vararg others: Button) {
        selected.setBackgroundColor(resources.getColor(R.color.green, null))
        others.forEach { it.setBackgroundColor(resources.getColor(android.R.color.darker_gray, null)) }
    }
}
