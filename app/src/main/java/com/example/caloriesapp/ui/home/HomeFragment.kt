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
import com.example.caloriesapp.activities.LoginActivity
import com.example.caloriesapp.R
import com.example.caloriesapp.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null

    private val binding get() = _binding!!

    private lateinit var homeViewModel: HomeViewModel

    private lateinit var userInfoTextView: TextView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        homeViewModel = ViewModelProvider(this).get(HomeViewModel::class.java)

        _binding = FragmentHomeBinding.inflate(inflater, container, false)

        val root: View = binding.root
        val textView: TextView = binding.userInfoText
        userInfoTextView = binding.userInfoText


        val userInfo = UserUtil.getUserInfoFromToken()
        println(userInfo)
        if (userInfo != null) {
            val userId = userInfo["userUuid"]
            val email = userInfo["email"]
            val name = userInfo["firstName"]

            val userInfoText = "User ID: $userId\nEmail: $email\nName: $name"
            userInfoTextView.text = userInfoText
        } else {
            userInfoTextView.text = "User info not available"
        }

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
        val sharedPreferences: SharedPreferences = requireActivity().getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.clear()
        editor.apply()

        val intent = Intent(requireContext(), LoginActivity::class.java)
        startActivity(intent)
        requireActivity().finish()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
