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
    private lateinit var navigator : Navigator
    //private var token : String? = null
    var firebaseData: FirebaseData = FirebaseData()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        firebaseData.initializeDbRef()

        setContentView(R.layout.activity_main)

        binding = ActivityMainBinding.inflate(layoutInflater)
        navigator = Navigator(supportFragmentManager, binding)
        navigator.setupListener()
        setContentView(binding.root)

        launcher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
                if (result.resultCode == Activity.RESULT_OK) {
                    token =
                        AuthorizationClient.getResponse(result.resultCode, result.data).accessToken
                    Log.d("token", token)
                }
            }
    }
}
