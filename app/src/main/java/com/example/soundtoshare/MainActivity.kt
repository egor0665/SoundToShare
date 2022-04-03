package com.example.soundtoshare

import android.os.Bundle
import android.util.Log
import androidx.activity.result.ActivityResultLauncher

import androidx.appcompat.app.AppCompatActivity
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager
import androidx.activity.result.contract.ActivityResultContracts
import com.example.soundtoshare.databases.FirestoreDatabase
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

    lateinit var launcher: ActivityResultLauncher<Intent>
    private lateinit var token: String
    private lateinit var navigator : Navigator
    //private var token : String? = null

    // FireStore example
    var fireStoreDatabase: FirestoreDatabase = FirestoreDatabase()

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        authVkLauncher = VK.login(this) { result: VKAuthenticationResult ->
            when (result) {
                is VKAuthenticationResult.Success -> {
                    Log.d("auth", VK.getUserId().toString())
                    Log.d("auth", result.token.accessToken)
                }
                is VKAuthenticationResult.Failed -> {
                    // User didn't pass authorization
                }
            }
        }

        setContentView(R.layout.activity_main)

        binding = ActivityMainBinding.inflate(layoutInflater)
        // Navigator
        navigator = Navigator(supportFragmentManager, binding)
        navigator.setupListener()
        // FireStore example
        fireStoreDatabase.getClosest(37.4219983,-122.084)

        WorkManager.getInstance(applicationContext)
                   .enqueue( OneTimeWorkRequest.Builder(VkWorker::class.java)
                                                .setInitialDelay(10, TimeUnit.SECONDS)
                                                .build())
    }
}

