package com.example.soundtoshare.main

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.View
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import androidx.work.WorkManager
import com.example.soundtoshare.BuildConfig
import com.example.soundtoshare.R
import com.example.soundtoshare.databinding.ActivityMainBinding
import com.vk.api.sdk.VK
import com.vk.api.sdk.VKApiConfig
import com.vk.api.sdk.auth.VKAuthenticationResult
import com.vk.api.sdk.auth.VKScope

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    private lateinit var navigator: HomeNavigator
    private lateinit var builder: NotificationCompat.Builder
    lateinit var authVkLauncher: ActivityResultLauncher<Collection<VKScope>>
    private var locationPermissionGranted = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)

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

    override fun onResume() {
        super.onResume()
        if (!locationPermissionGranted)
            getLocationPermission()
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
        WorkManager.getInstance(applicationContext).cancelAllWorkByTag("VKMusic")

        super.onDestroy()
    }

    fun navigate(screen: Screen) {
        navigator.setScreen(screen)
    }

    private fun initVK() {
        VK.setConfig(VKApiConfig(applicationContext, BuildConfig.vk_id.toInt()))
        authVkLauncher = VK.login(this) { result: VKAuthenticationResult ->
            when (result) {
                is VKAuthenticationResult.Success -> {
                    Log.d("VK_auth", VK.getUserId().toString())
                    binding.navView.selectedItemId = R.id.home
                    binding.navView.visibility = View.VISIBLE
                }
                is VKAuthenticationResult.Failed -> {
                    Log.d("VK_auth", "FAILURE")
                }
            }
        }
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

        if (VK.isLoggedIn()) {
            if (selectedId != null)
                binding.navView.selectedItemId = selectedId
            navigator.setScreen(Screen.Home)
        } else {
            binding.navView.visibility = View.INVISIBLE
            navigator.setScreen(Screen.SignIn)
        }
    }

    fun vkSignOut() {
        VK.logout()
        VK.clearAccessToken(this)
        navigator.setScreen(Screen.SignIn)
        binding.navView.visibility = View.INVISIBLE
    }

    private fun initNotification() {

        builder = NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_tmplogo)
            .setContentText("Sharing your music taste...")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setAutoCancel(true)

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

    private val requestMultiplePermissions =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
            locationPermissionGranted = true
            permissions.entries.forEach {
                if (!it.value) locationPermissionGranted = false
            }
            if (!locationPermissionGranted) {
                startDeniedPermissionAlert()
            }
        }

    private fun getLocationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            if (ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.ACCESS_BACKGROUND_LOCATION
                )
                != PackageManager.PERMISSION_GRANTED
            ) {
                startDeniedPermissionAlert()
            } else
                locationPermissionGranted = true
        } else {
            if (ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.ACCESS_FINE_LOCATION
                )
                != PackageManager.PERMISSION_GRANTED
            ) {
                requestMultiplePermissions.launch(
                    arrayOf(
                        Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.ACCESS_FINE_LOCATION
                    )
                )
            }
        }
    }

    private fun startDeniedPermissionAlert() {
        val alertDialogBuilder: AlertDialog.Builder = AlertDialog.Builder(this).apply {
            setTitle("Permission needed")
            setMessage("Please, Allow all the time location permission for app to work")
            setPositiveButton("Open Setting") { _, _ ->
                val uri: Uri = Uri.fromParts("package", packageName, null)
                val intent = Intent().apply {
                    action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
                    data = uri
                    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY)
                    addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS)
                }
                startActivity(intent)
            }
            setNegativeButton("Quit") { _, _ ->
                startActivity(Intent(Intent.ACTION_MAIN).addCategory(Intent.CATEGORY_HOME))
            }
        }

        val dialog: AlertDialog = alertDialogBuilder.create()
        dialog.setCanceledOnTouchOutside(false)
        dialog.show()

        val mPositiveButton = dialog.getButton(AlertDialog.BUTTON_POSITIVE)
        mPositiveButton.setOnClickListener {
            dialog.cancel()
            val uri: Uri = Uri.fromParts("package", packageName, null)
            val intent = Intent().apply {
                action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
                data = uri
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY)
                addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS)
            }
            startActivity(intent)
        }
    }

    companion object {
        const val CHANNEL_ID = "SoundToShare"
        const val notificationId = 1
    }
}
