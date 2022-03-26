package com.example.soundtoshare

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.soundtoshare.databinding.ActivityMainBinding
import com.example.soundtoshare.fragments.home.HomeFragment
import com.example.soundtoshare.fragments.map.MapFragment
import com.example.soundtoshare.fragments.settings.SettingsFragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navView: BottomNavigationView = binding.navView
        navView.setOnItemSelectedListener(){
            when (it.itemId)
            {
                R.id.home -> supportFragmentManager.beginTransaction().replace(R.id.nav_host_fragment_activity_main, HomeFragment()).commit()
                R.id.map -> supportFragmentManager.beginTransaction().replace(R.id.nav_host_fragment_activity_main, MapFragment()).commit()
                R.id.settings-> supportFragmentManager.beginTransaction().replace(R.id.nav_host_fragment_activity_main, SettingsFragment()).commit()
            }
            return@setOnItemSelectedListener true
        }
        navView.selectedItemId = R.id.home
    }
}
