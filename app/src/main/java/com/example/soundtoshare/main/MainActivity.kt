package com.example.soundtoshare.main

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.soundtoshare.BuildConfig
import com.example.soundtoshare.R
import com.example.soundtoshare.databinding.ActivityMainBinding
import com.vk.api.sdk.VK
import com.vk.api.sdk.VKApiConfig

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    private lateinit var navigator: HomeNavigator
    private lateinit var builder: NotificationCompat.Builder


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)

        initRepos()
        initVK()
        initNavigator(savedInstanceState?.getInt("id"))
        initNotification()
        setContentView(binding.root)
    }

    override fun onStart() {
        super.onStart()
        with(NotificationManagerCompat.from(this)) {
            // notificationId is a unique int for each notification that you must define
            cancel(notificationId)
        }
    }

    override fun onStop() {
        super.onStop()
        with(NotificationManagerCompat.from(this)) {
            notify(notificationId, builder.build())
        }
    }

    override fun onDestroy() {
        with(NotificationManagerCompat.from(this)) {
            cancel(notificationId)
        }
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

    private fun initNotification() {

         builder = NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_tmplogo)
            .setContentText("Uploading your music data...")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
             .setOngoing(true)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "SoundToShare"
            val descriptionText = "User info updates"
            val importance = NotificationManager.IMPORTANCE_LOW
            val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
                description = descriptionText
            }
            // Register the channel with the system
            val notificationManager: NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    companion object {
        const val CHANNEL_ID = "SoundToShare"
        const val notificationId = 1
    }

}
