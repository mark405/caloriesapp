package com.example.caloriesapp.ui.home

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.caloriesapp.LoginActivity
import com.example.caloriesapp.R
import com.example.caloriesapp.databinding.FragmentHomeBinding
import com.example.caloriesapp.network.RetrofitInstance.appContext

class HomeFragment : Fragment() {

    // ViewBinding reference to be used to access the fragment's views
    private var _binding: FragmentHomeBinding? = null

    // This property is only valid between onCreateView and onDestroyView.
    private val binding get() = _binding!!

    // This ViewModel will be used to observe changes to the text displayed
    private lateinit var homeViewModel: HomeViewModel

    // Declare a TextView for displaying user info
    private lateinit var userInfoTextView: TextView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Initialize the ViewModel
        homeViewModel = ViewModelProvider(this).get(HomeViewModel::class.java)

        // Initialize the binding object
        _binding = FragmentHomeBinding.inflate(inflater, container, false)

        // Use the binding to access views and update them based on the ViewModel's data
        val root: View = binding.root
        val textView: TextView = binding.userInfoText
        userInfoTextView = binding.userInfoText // Assume userInfoText is defined in your XML layout

        val token = appContext?.let { SharedPreferencesManager.getAccessToken(it) }

        // Example accessToken - replace with actual token from SharedPreferences or your logic
        // Decode the token to retrieve user information
        val userInfo = token?.let { TokenUtils.getUserInfoFromToken(it) }
        println(userInfo)
        // Display the user info in the TextView
        if (userInfo != null) {
            val userId = userInfo["userId"]
            val email = userInfo["email"]
            val name = userInfo["name"]

            // Combine user info into a single string and display it
            val userInfoText = "User ID: $userId\nEmail: $email\nName: $name"
            userInfoTextView.text = userInfoText
        } else {
            userInfoTextView.text = "User info not available"
        }

        // Observe changes in the ViewModel's text and update the TextView
        homeViewModel.text.observe(viewLifecycleOwner) { newText ->
            textView.text = newText
        }

        val logoutButton: Button = binding.root.findViewById(R.id.logoutButton)
        logoutButton.setOnClickListener {
            logout()
        }

        return root
    }

    private fun logout() {
        // Clear the shared preferences or session data
        val sharedPreferences: SharedPreferences = requireActivity().getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.clear()  // Remove all stored data
        editor.apply()

        // Navigate back to the login screen
        val intent = Intent(requireContext(), LoginActivity::class.java)
        startActivity(intent)
        requireActivity().finish()  // Close the current activity (HomeFragment)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        // Nullify the binding reference to prevent memory leaks
        _binding = null
    }
}
