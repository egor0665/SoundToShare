package com.example.soundtoshare.main

import android.Manifest
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
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
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

        setContentView(binding.root)
    }

    override fun onResume() {
        super.onResume()
        if (!locationPermissionGranted)
            getLocationPermission()
    }

    private fun navigate(screen: Screen) {
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

    private fun getLocationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q)
            checkSelfPermissionTrue()
        else
            checkSelfPermissionFalse()
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    private fun checkSelfPermissionTrue() {
        val accessBackgroundLocation = ContextCompat.checkSelfPermission(
            applicationContext,
            Manifest.permission.ACCESS_BACKGROUND_LOCATION
        )
        if (accessBackgroundLocation
            != PackageManager.PERMISSION_GRANTED
        )
            startDeniedPermissionAlert()
        else
            locationPermissionGranted = true
    }

    private fun checkSelfPermissionFalse() {
        val accessFineLocation = ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.ACCESS_FINE_LOCATION
        )
        if (accessFineLocation != PackageManager.PERMISSION_GRANTED)
            requestMultiplePermissions.launch(
                arrayOf(
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_FINE_LOCATION
                )
            )
    }

    private val requestMultiplePermissions =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) {
            permissions ->
            locationPermissionGranted = true
            permissions.entries.forEach {
                if (!it.value) locationPermissionGranted = false
            }
            if (!locationPermissionGranted) {
                startDeniedPermissionAlert()
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
