package com.example.soundtoshare

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.soundtoshare.databinding.ActivityMainBinding
import com.example.soundtoshare.fragments.home.HomeFragment
import com.example.soundtoshare.fragments.map.MapFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.spotify.sdk.android.auth.AuthorizationClient

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var launcher: ActivityResultLauncher<Intent>
    private var token : String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        launcher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
                result: ActivityResult ->
            if (result.resultCode == Activity.RESULT_OK)
                token = AuthorizationClient.getResponse(result.resultCode, result.data).accessToken
        }

        val navView: BottomNavigationView = binding.navView
        val navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main)
        val appBarConfiguration = AppBarConfiguration(setOf(R.id.home, R.id.map, R.id.settings))
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        /*binding.spotifyLoginBtn.setOnClickListener {
            launcher.launch(AuthorizationClient.createLoginActivityIntent(this, SpotifyAPI.getAuthenticationRequest()))
        }

        binding.spotifyLogoutBtn.setOnClickListener {
            AuthorizationClient.clearCookies(applicationContext)
            token = null
        }

        binding.spotifyMusicBtn.setOnClickListener(){
            SpotifyAPI.fetchSpotifyMusic(token)
        }*/
    }
}
