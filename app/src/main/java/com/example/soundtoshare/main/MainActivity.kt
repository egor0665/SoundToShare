package com.example.soundtoshare.main

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager
import com.example.soundtoshare.BuildConfig
import com.example.soundtoshare.R
import com.example.soundtoshare.databinding.ActivityMainBinding
//import com.example.soundtoshare.dependency_injection.domainDI
//import com.example.soundtoshare.dependency_injection.fragmentDI
import com.example.soundtoshare.repositories.SharedPreferencesRepository
import com.example.soundtoshare.workers.VkWorker
import com.vk.api.sdk.VK
import com.vk.api.sdk.VKApiConfig
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    private lateinit var navigator: HomeNavigator

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)

        initRepos()
        initVK()
        initNavigator()
        initWorkers()
        setContentView(binding.root)
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

    private fun initNavigator() {
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
            navigator.apply {
                if (VK.isLoggedIn()) {
                    navigator.setScreen(Screen.Home)
                } else {
                    binding.navView.visibility = View.INVISIBLE
                    navigator.setScreen(Screen.SignIn)
                }
            }
        }

        private fun initWorkers() {
            WorkManager.getInstance(applicationContext)
                .enqueue(
                    OneTimeWorkRequest.Builder(VkWorker::class.java)
                        .addTag("VKMusic")
                        .setInitialDelay(10, TimeUnit.SECONDS)
                        .build()
                )
        }

        fun vkSignOut() {
            VK.logout()
            VK.clearAccessToken(this)
            navigator.setScreen(Screen.SignIn)
        }
    }
