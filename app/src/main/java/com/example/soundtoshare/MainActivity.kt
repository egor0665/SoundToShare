package com.example.soundtoshare

import android.os.Bundle
import android.util.Log
import androidx.activity.result.ActivityResultLauncher
import androidx.appcompat.app.AppCompatActivity
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager
import com.example.soundtoshare.databinding.ActivityMainBinding
import com.example.soundtoshare.fragments.home.HomeFragment
import com.example.soundtoshare.fragments.map.MapFragment
import com.example.soundtoshare.fragments.settings.SettingsFragment
import com.example.soundtoshare.workers.VkWorker
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.vk.api.sdk.VK
import com.vk.api.sdk.auth.VKAuthenticationResult
import com.vk.api.sdk.auth.VKScope
import java.util.concurrent.TimeUnit


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    internal lateinit var authVkLauncher:  ActivityResultLauncher<Collection<VKScope>>
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        authVkLauncher = VK.login(this) { result : VKAuthenticationResult ->
            when (result) {
                is VKAuthenticationResult.Success -> {
                    Log.d("auth",VK.getUserId().toString())
                    Log.d("auth", result.token.accessToken)
                }
                is VKAuthenticationResult.Failed -> {
                    // User didn't pass authorization
                }
            }
        }

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navView: BottomNavigationView = binding.navView
        navView.setOnItemSelectedListener{
            when (it.itemId)
            {
                R.id.home -> supportFragmentManager.beginTransaction().replace(R.id.nav_host_fragment_activity_main, HomeFragment()).commit()
                R.id.map -> supportFragmentManager.beginTransaction().replace(R.id.nav_host_fragment_activity_main, MapFragment()).commit()
                R.id.settings -> supportFragmentManager.beginTransaction().replace(R.id.nav_host_fragment_activity_main, SettingsFragment()).commit()
            }
            return@setOnItemSelectedListener true
        }
        navView.selectedItemId = R.id.home

        WorkManager.getInstance(applicationContext)
                   .enqueue( OneTimeWorkRequest.Builder(VkWorker::class.java)
                                                .setInitialDelay(10, TimeUnit.SECONDS)
                                                .build())
    }
}

