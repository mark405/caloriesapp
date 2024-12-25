package com.example.caloriesapp.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import com.example.caloriesapp.databinding.FragmentEditFieldSelectionBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class EditSelectionBottomSheet(
    private val title: String,
    private val currentValue: String,
    private val options: List<String>,
    private val onSave: (String) -> Unit
) : BottomSheetDialogFragment() {

    private var _binding: FragmentEditFieldSelectionBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentEditFieldSelectionBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Set the title of the dialog
        binding.tvFieldTitle.text = title

        // Dynamically add RadioButtons for each option
        options.forEach { option ->
            val radioButton = RadioButton(requireContext()).apply {
                text = option
                isChecked = option == currentValue // Set current value as checked
            }
            binding.rgOptions.addView(radioButton)
        }

        // Handle the "Done" button click
        binding.btnDone.setOnClickListener {
            val selectedOptionId = binding.rgOptions.checkedRadioButtonId
            val selectedOption = view.findViewById<RadioButton>(selectedOptionId)?.text.toString()

            // Pass the selected option back to the caller
            onSave(selectedOption)
            dismiss() // Close the bottom sheet
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
