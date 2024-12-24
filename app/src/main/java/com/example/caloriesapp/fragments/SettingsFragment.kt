package com.example.caloriesapp.fragments

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.caloriesapp.activities.LoginActivity
import com.example.caloriesapp.databinding.FragmentSettingsBinding
import com.example.caloriesapp.network.RetrofitInstance
import com.example.caloriesapp.network.UserDetails
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SettingsFragment : Fragment() {

    private var _binding: FragmentSettingsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Fetch and display user details
        fetchUserDetails()

        // Handle button clicks
        binding.btnLogout.setOnClickListener {
            logout()
        }

        binding.btnContactUs.setOnClickListener {
            Toast.makeText(requireContext(), "Contact us clicked", Toast.LENGTH_SHORT).show()
        }

        binding.btnAboutApp.setOnClickListener {
            Toast.makeText(requireContext(), "About app clicked", Toast.LENGTH_SHORT).show()
        }

        binding.btnSettings.setOnClickListener {
            Toast.makeText(requireContext(), "Settings clicked", Toast.LENGTH_SHORT).show()
        }
    }

    private fun fetchUserDetails() {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = RetrofitInstance.baseApi.getUserDetails()
                if (response.isSuccessful) {
                    val userDetails = response.body()
                    withContext(Dispatchers.Main) {
                        bindUserDetails(userDetails)
                    }
                } else {
                    withContext(Dispatchers.Main) {
                        Toast.makeText(
                            requireContext(),
                            "Failed to load user details: ${response.message()}",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(requireContext(), "Error: ${e.localizedMessage}", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun bindUserDetails(userDetails: UserDetails?) {
        userDetails?.let {
            binding.profileName.text = it.name
            binding.profileEmail.text = it.email
            binding.tvCalorieIntake.text = "${it.calorieIntake} Cal"
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun logout() {
        // Clear shared preferences or token storage
        val sharedPreferences: SharedPreferences =
            requireActivity().getSharedPreferences("AppPrefs", android.content.Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.clear() // Clear all stored preferences
        editor.apply()

        // Redirect to LoginActivity
        val intent = Intent(requireContext(), LoginActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK // Clear back stack
        startActivity(intent)

        // Show logout message
        Toast.makeText(requireContext(), "Logged out successfully", Toast.LENGTH_SHORT).show()
    }
}
