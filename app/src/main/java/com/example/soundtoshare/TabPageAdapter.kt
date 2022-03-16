package com.example.soundtoshare

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.soundtoshare.fragments.HomeFragment
import com.example.soundtoshare.fragments.MapFragment
import com.example.soundtoshare.fragments.SettingsFragment

class TabPageAdapter(activity: FragmentActivity, private val tabCount: Int) : FragmentStateAdapter(activity)
{
    override fun getItemCount(): Int = tabCount

    override fun createFragment(position: Int): Fragment
    {
        return when (position)
        {

            0 -> HomeFragment()
            1 -> MapFragment()
            2 -> SettingsFragment()
            else -> HomeFragment()
        }
    }

}