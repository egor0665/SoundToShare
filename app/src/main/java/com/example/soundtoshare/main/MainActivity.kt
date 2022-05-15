package com.example.soundtoshare.main

//import com.example.soundtoshare.dependency_injection.domainDI
//import com.example.soundtoshare.dependency_injection.fragmentDI

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.soundtoshare.BuildConfig
import com.example.soundtoshare.R
import com.example.soundtoshare.databinding.ActivityMainBinding
import com.vk.api.sdk.VK
import com.vk.api.sdk.VKApiConfig

import androidx.core.content.ContextCompat
import com.example.soundtoshare.BuildConfig
import com.example.soundtoshare.R
import com.example.soundtoshare.databinding.ActivityMainBinding
import com.example.soundtoshare.fragments.map.MapWrapperLayout
import com.example.soundtoshare.fragments.map.OnInfoWindowElemTouchListener
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.vk.api.sdk.VK
import com.vk.api.sdk.VKApiConfig

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    private lateinit var navigator: HomeNavigator


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)

        initRepos()
        initVK()
        initNavigator(savedInstanceState?.getInt("id"))
//        initWorkers()
        setContentView(binding.root)
    }

    override fun onDestroy() {
        super.onDestroy()

    }

    private fun initRepos() {
        //SharedPreferencesRepository.initialize(this)
    }

    fun navigate(screen: Screen) {
        navigator.setScreen(screen)
    }

    private fun initVK() {
        VK.setConfig(VKApiConfig(applicationContext, BuildConfig.vk_id.toInt()))
    }

    private fun initNavigator(selectedId: Int?) {
        navigator = HomeNavigator(supportFragmentManager, R.id.nav_host_fragment_activity_main)
        binding.navView.setOnItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.home -> navigate(Screen.Home)
                R.id.map -> navigate(Screen.Map)
                R.id.settings -> navigate(Screen.Settings)
                else -> {
                    Log.d("Navigator", "Something went wrong")
                    return@setOnItemSelectedListener false
                }
            }
            return@setOnItemSelectedListener true
        }
            if (selectedId != null) {
                binding.navView.selectedItemId = selectedId
            } else {
                if (VK.isLoggedIn()) {
                    navigator.setScreen(Screen.Home)
                } else {
                    binding.navView.visibility = View.INVISIBLE
                    navigator.setScreen(Screen.SignIn)
                }
            }
        }

        fun vkSignOut() {
            VK.logout()
            VK.clearAccessToken(this)
            navigator.setScreen(Screen.SignIn)
        }


}
