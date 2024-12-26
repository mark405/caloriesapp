package com.example.caloriesapp.adapters

import com.example.caloriesapp.fragments.AgeFragment
import com.example.caloriesapp.fragments.GenderFragment
import com.example.caloriesapp.fragments.WeightFragment
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.caloriesapp.fragments.GoalFragment
import com.example.caloriesapp.fragments.ActivityFragment
import com.example.caloriesapp.fragments.HeightFragment

class UserOptionsAdapter(fragmentActivity: FragmentActivity) : FragmentStateAdapter(fragmentActivity) {

    private val fragments = listOf(
        GenderFragment(),
        AgeFragment(),
        HeightFragment(),
        WeightFragment(),
        GoalFragment(),
        ActivityFragment(),
    )

    override fun getItemCount(): Int = fragments.size

    override fun createFragment(position: Int): Fragment {
        return fragments[position]
    }
}
