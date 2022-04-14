package com.example.soundtoshare

import android.os.Bundle
import android.util.Log
import androidx.activity.result.ActivityResultLauncher
import androidx.appcompat.app.AppCompatActivity
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager
import com.example.soundtoshare.apis.VkAPI
import com.example.soundtoshare.databases.FirestoreDatabase
import com.example.soundtoshare.databinding.ActivityMainBinding
import com.example.soundtoshare.workers.VkWorker
import com.vk.api.sdk.VK
import com.vk.api.sdk.VKApiConfig
import com.vk.api.sdk.auth.VKAuthenticationResult
import com.vk.api.sdk.auth.VKScope
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    internal lateinit var authVkLauncher: ActivityResultLauncher<Collection<VKScope>>
    private lateinit var navigator: Navigator

    // FireStore example
//    var fireStoreDatabase: FirestoreDatabase = FirestoreDatabase()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        VK.setConfig(VKApiConfig(applicationContext,BuildConfig.vk_id.toInt()))

        authVkLauncher = VK.login(this) { result: VKAuthenticationResult ->
            when (result) {
                is VKAuthenticationResult.Success -> {
                    Log.d("VK_auth", VK.getUserId().toString())
                }
                is VKAuthenticationResult.Failed -> {
                    Log.d("VK_auth", "FAILURE")
                }
            }
        }

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        // Navigator
        navigator = Navigator(supportFragmentManager, binding)
        navigator.setupListener()
        // FireStore example
//        fireStoreDatabase.getClosest(37.4219983, -122.084)

        WorkManager.getInstance(applicationContext)
            .enqueue(
                OneTimeWorkRequest.Builder(VkWorker::class.java)
                    .addTag("VKMusic")
                    .setInitialDelay(10, TimeUnit.SECONDS)
                    .build()
            )
    }
}

