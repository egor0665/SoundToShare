package com.example.soundtoshare

import android.util.Log
import androidx.fragment.app.FragmentManager
import com.example.soundtoshare.databinding.ActivityMainBinding
import com.example.soundtoshare.fragments.home.HomeFragment
import com.example.soundtoshare.fragments.map.MapFragment
import com.example.soundtoshare.fragments.settings.SettingsFragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class Navigator(private val supportFragmentManager: FragmentManager, binding : ActivityMainBinding) {
    private val navView: BottomNavigationView = binding.navView

    fun setupListener(){
        navView.setOnItemSelectedListener{ menuItem ->
            Log.d("ScreenNavigator", menuItem.itemId.toString())
            val currentFragment = supportFragmentManager.fragments.firstOrNull{ it.isVisible }
            Log.d("ScreenNavigator", currentFragment.toString())
            val newFragment = supportFragmentManager.findFragmentByTag(menuItem.itemId.toString())
            val fragmentTransaction = supportFragmentManager.beginTransaction()
            if (currentFragment != null){
                if (currentFragment.tag != menuItem.itemId.toString()){
                    fragmentTransaction.hide(currentFragment)
                    if (newFragment != null){
                        fragmentTransaction.show(newFragment)
                    }
                    else{
                        when (menuItem.itemId)
                        {
                            R.id.home -> fragmentTransaction.add(R.id.nav_host_fragment_activity_main, HomeFragment(), menuItem.itemId.toString())
                            R.id.map -> fragmentTransaction.add(R.id.nav_host_fragment_activity_main, MapFragment(), menuItem.itemId.toString())
                            R.id.settings-> fragmentTransaction.add(R.id.nav_host_fragment_activity_main, SettingsFragment(), menuItem.itemId.toString())
                        }
                    }
                }
            }
            else{
                fragmentTransaction.replace(R.id.nav_host_fragment_activity_main, HomeFragment(), R.id.home.toString())
            }
            fragmentTransaction.commit()
            Log.d("ScreenNavigator", supportFragmentManager.fragments.toString())
            return@setOnItemSelectedListener true
        }
        navView.selectedItemId = R.id.home
    }
}