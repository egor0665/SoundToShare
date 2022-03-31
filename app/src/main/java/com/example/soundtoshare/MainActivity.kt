package com.example.soundtoshare

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import com.example.soundtoshare.databases.FirebaseData
import com.example.soundtoshare.databinding.ActivityMainBinding
import com.example.soundtoshare.fragments.home.HomeFragment
import com.example.soundtoshare.fragments.map.MapFragment
import com.example.soundtoshare.fragments.settings.SettingsFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.spotify.sdk.android.auth.AuthorizationClient


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    lateinit var launcher: ActivityResultLauncher<Intent>
    private lateinit var token: String
    var firebaseData: FirebaseData = FirebaseData()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        firebaseData.initializeDbRef()

        setContentView(R.layout.activity_main)

        launcher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
                result: ActivityResult ->
            if (result.resultCode == Activity.RESULT_OK) {
                token = AuthorizationClient.getResponse(result.resultCode, result.data).accessToken
                Log.d("token", token)
            }
        }

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
