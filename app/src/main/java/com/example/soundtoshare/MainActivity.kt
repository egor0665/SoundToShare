package com.example.soundtoshare

import android.os.Bundle
import android.util.Log
import androidx.activity.result.ActivityResultLauncher
import androidx.appcompat.app.AppCompatActivity
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager
import com.example.soundtoshare.databinding.ActivityMainBinding
import com.example.soundtoshare.fragments.home.HomeFragment
import com.example.soundtoshare.fragments.home.SignInFragment
import com.example.soundtoshare.workers.VkWorker
import com.vk.api.sdk.VK
import com.vk.api.sdk.VKApiConfig
import com.vk.api.sdk.auth.VKAuthenticationResult
import com.vk.api.sdk.auth.VKScope
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {

//    //thema
//    private var xyz: Switch? = null
//    internal lateinit var sharedpref: SharedPref

    private lateinit var binding: ActivityMainBinding
    private lateinit var navigator: Navigator
    internal lateinit var authVkLauncher: ActivityResultLauncher<Collection<VKScope>>
    var incognito = false

    override fun onCreate(savedInstanceState: Bundle?) {
//        //thema
//        sharedpref = SharedPref(this)
//        if (sharedpref.loadNightModeState()==true) {
//            setTheme(R.style.DarkThema)
//        } else
//            setTheme(R.style.AppThema)

        super.onCreate(savedInstanceState)

//        //thema
//        xyz = findViewById<View>(R.id.enableDark) as Switch?
//        if (sharedpref.loadNightModeState() == true) {
//            xyz!!.isChecked=true
//        }
//        xyz!!.setOnCheckedChangeListener{ buttonView, isChecked ->
//            if (isChecked) {
//                sharedpref.setNightModeState(true)
//                restartApp()
//            } else {
//                sharedpref.setNightModeState(false)
//                restartApp()
//            }
//
//
//        }

        VK.setConfig(VKApiConfig(applicationContext, BuildConfig.vk_id.toInt()))

        authVkLauncher = VK.login(this) { result: VKAuthenticationResult ->
            when (result) {
                is VKAuthenticationResult.Success -> {
                    Log.d("VK_auth", VK.getUserId().toString())
                    navigator.setFragment(HomeFragment())
                }
                is VKAuthenticationResult.Failed -> {
                    Log.d("VK_auth", "FAILURE")
                }
            }
        }

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

//        binding.navView.setBackgroundColor(Color.BLACK)
        // Navigator
        navigator = Navigator(supportFragmentManager, binding)
        navigator.setupListener()

        if (VK.isLoggedIn()) navigator.setFragment(HomeFragment())
        else navigator.setFragment(SignInFragment())

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
        navigator.setFragment(SignInFragment())
    }

//    //thema
//    fun restartApp() {
//        val i = Intent (getApplicationContext(), MainActivity::class.java)
//        startActivity(i)
//        finish()
//    }
}
