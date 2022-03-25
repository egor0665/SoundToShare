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
import com.example.soundtoshare.databinding.ActivityMainBinding
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

    private lateinit var binding: FragmentSignInBinding
    private lateinit var launcher: ActivityResultLauncher<Intent>
    private var launcherGoogle = GoogleAuth.SignInLauncher.newInstance(this)
    /*
    private lateinit var googleSingInClient: GoogleSignInClient
    private lateinit var firebaseAuth: FirebaseAuth

    private companion object {
        private const val RS_SIGN_IN = 100
        private const val TAG = "GOOGLE_SIGN_IN_TAG"
    }
*/
    private lateinit var oneTapClient: SignInClient
    private lateinit var signInRequest: BeginSignInRequest


    private var token : String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

       binding = FragmentSignInBinding.inflate(layoutInflater)
       setContentView(binding.root)

        //setContentView(R.layout.activity_main)
       // setUpTabBar()

        //binding = ActivityMainBinding.inflate(layoutInflater)
        //setContentView(binding.root)

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
        binding.google.setOnClickListener(){
            GoogleAuth.signIn(this, launcherGoogle)
        }
        binding.spotify.setOnClickListener(){
            GoogleAuth.signOut(this)
        }

    }


    private fun setUpTabBar()
    {
        val adapter = TabPageAdapter(this, tabLayout.tabCount)
        viewPager.adapter = adapter

        viewPager.registerOnPageChangeCallback(object: ViewPager2.OnPageChangeCallback()
        {
            override fun onPageSelected(position: Int) {
                tabLayout.selectTab(tabLayout.getTabAt(position))
            }
        })

        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener
        {
            override fun onTabSelected(tab: TabLayout.Tab)
            {
                viewPager.currentItem = tab.position
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {}

            override fun onTabReselected(tab: TabLayout.Tab?) {}
        })
    }

    private fun checkUser() {

    }


}
