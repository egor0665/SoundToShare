package com.example.soundtoshare

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import com.example.soundtoshare.databinding.ActivityMainBinding
import com.example.soundtoshare.fragments.HomeFragment
import com.example.soundtoshare.fragments.SpotifyAPI
import com.spotify.sdk.android.auth.AuthorizationClient

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var launcher: ActivityResultLauncher<Intent>
    private var token : String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        supportFragmentManager.beginTransaction()
            .replace(R.id.main_fragment_container, HomeFragment())
            .commit()

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        launcher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
                result: ActivityResult ->
            if (result.resultCode == Activity.RESULT_OK)
                token = AuthorizationClient.getResponse(result.resultCode, result.data).accessToken
        }

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
//class MainActivity : AppCompatActivity() {
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_main)
//    }
//}