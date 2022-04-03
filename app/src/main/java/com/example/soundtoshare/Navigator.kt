package com.example.soundtoshare

import android.util.Log
import androidx.fragment.app.FragmentManager
import com.example.soundtoshare.databinding.ActivityMainBinding
import com.example.soundtoshare.fragments.home.HomeFragment
import com.example.soundtoshare.fragments.map.MapFragment
import com.example.soundtoshare.fragments.settings.SettingsFragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class Navigator(supportFragmentManager : FragmentManager, binding : ActivityMainBinding) {
    private val supportFragmentManager : FragmentManager = supportFragmentManager
    private val navView: BottomNavigationView = binding.navView

    fun setupListener(){
        navView.setOnItemSelectedListener(){
            Log.d("ScreenNavigator", it.itemId.toString())
            val currentFragment = supportFragmentManager.fragments.firstOrNull{ it.isVisible }
            val newFragment = supportFragmentManager.findFragmentByTag(it.itemId.toString())
            val fragmentTransaction = supportFragmentManager.beginTransaction()
            if (currentFragment != null){
                if (currentFragment.tag != it.itemId.toString()){
                    fragmentTransaction.hide(currentFragment)
                    if (newFragment != null){
                        fragmentTransaction.show(newFragment)
                    }
                    else{
                        when (it.itemId)
                        {
                            R.id.home -> fragmentTransaction.add(R.id.nav_host_fragment_activity_main, HomeFragment(), it.itemId.toString())
                            R.id.map -> fragmentTransaction.add(R.id.nav_host_fragment_activity_main, MapFragment(), it.itemId.toString())
                            R.id.settings-> fragmentTransaction.add(R.id.nav_host_fragment_activity_main, SettingsFragment(), it.itemId.toString())
                        }
                    }
                }
            }
            else{
                fragmentTransaction.add(R.id.nav_host_fragment_activity_main, HomeFragment(), R.id.home.toString())
            }
            fragmentTransaction.commit()
            // Log.d("ScreenNavigator", supportFragmentManager.fragments.toString())
            return@setOnItemSelectedListener true
        }
        navView.selectedItemId = R.id.home
    }
}