package com.example.soundtoshare

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import kotlinx.android.synthetic.main.activity_main.*

import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import com.example.soundtoshare.apis.GoogleAuth
import com.example.soundtoshare.databases.FirebaseData
import com.example.soundtoshare.databinding.ActivityMainBinding
import com.example.soundtoshare.fragments.home.HomeFragment
import com.example.soundtoshare.fragments.map.MapFragment
import com.example.soundtoshare.fragments.settings.SettingsFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.example.soundtoshare.databinding.FragmentHomeBinding
import com.example.soundtoshare.databinding.FragmentSignInBinding
import com.example.soundtoshare.fragments.sign_in.SignInFragment
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.identity.Identity
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.components.BuildConfig
import com.spotify.sdk.android.auth.AuthorizationClient
import kotlinx.android.synthetic.main.fragment_sign_in.*
import java.lang.Exception

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    lateinit var launcher: ActivityResultLauncher<Intent>
    private lateinit var token: String
    //private var token : String? = null
    var firebaseData: FirebaseData = FirebaseData()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        firebaseData.initializeDbRef()

        setContentView(R.layout.activity_main)
        //setUpTabBar()

        //binding = ActivityMainBinding.inflate(layoutInflater)
        //setContentView(binding.root)
    //private lateinit var token: String

        launcher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
                result: ActivityResult ->
            if (result.resultCode == Activity.RESULT_OK) {
                token = AuthorizationClient.getResponse(result.resultCode, result.data).accessToken
                Log.d("token", token)
            }
        }
            /*
        binding.spotifyLogoutBtn.setOnClickListener {
            AuthorizationClient.clearCookies(applicationContext)
            token = null
        }

        binding.spotifyMusicBtn.setOnClickListener(){
            SpotifyAPI.fetchSpotifyMusic(token)
        }*/
        binding.google.setOnClickListener(){
            GoogleAuth.signIn(this, launcherGoogle)
        }
        binding.spotify.setOnClickListener(){
            GoogleAuth.signOut(this)
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

    private fun checkUser() {

    }


}
