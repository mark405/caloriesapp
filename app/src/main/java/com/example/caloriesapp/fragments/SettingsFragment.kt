package com.example.caloriesapp.fragments

import SharedPreferencesManager
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.caloriesapp.activities.AboutAppActivity
import com.example.caloriesapp.activities.LoginActivity
import com.example.caloriesapp.activities.UserOptionsOverviewActivity
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
        context?.let { RetrofitInstance.init(it) }

        // Fetch and display user details
        fetchUserDetails()

        // Navigate to UserOptionsOverviewActivity
        binding.btnMe.setOnClickListener {
            val intent = Intent(requireContext(), UserOptionsOverviewActivity::class.java)
            startActivity(intent)
        }

        // Handle button clicks
        binding.btnLogout.setOnClickListener { logout() }
        binding.btnAboutApp.setOnClickListener {
            val intent = Intent(requireContext(), AboutAppActivity::class.java)
            startActivity(intent)
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
        showToast("Logged out successfully")
    }

    private fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }
}
