package com.example.soundtoshare

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import kotlinx.android.synthetic.main.activity_main.*
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
        setContentView(R.layout.activity_main)
        setUpTabBar()

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
}
//class MainActivity : AppCompatActivity() {
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_main)
//    }
//}