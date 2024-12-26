package com.example.caloriesapp.fragments

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.caloriesapp.databinding.FragmentEditFieldBottomSheetBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class EditFieldBottomSheet(
    private val fieldName: String,
    private val currentValue: String,
    private val onSave: (String) -> Unit
) : BottomSheetDialogFragment() {

    private var _binding: FragmentEditFieldBottomSheetBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentEditFieldBottomSheetBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Set field name and current value
        binding.tvFieldName.text = fieldName
        binding.etFieldValue.setText(currentValue)

        // Handle Save button click
        binding.btnDone.setOnClickListener {
            val newValue = binding.etFieldValue.text.toString()
            onSave(newValue)
            dismiss()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
