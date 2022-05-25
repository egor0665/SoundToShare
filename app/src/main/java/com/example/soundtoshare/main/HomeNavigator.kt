package com.example.soundtoshare.main

import androidx.fragment.app.FragmentManager
import com.example.soundtoshare.fragments.home.Home
import com.example.soundtoshare.fragments.home.SignIn
import com.example.soundtoshare.fragments.map.Map
import com.example.soundtoshare.fragments.settings.Settings

open class HomeNavigator(fragmentManager: FragmentManager, containerId: Int) :
    Navigator(fragmentManager, containerId) {

    override fun setScreen(screen: Screen) {
        when (screen) {
            is Screen.Home -> {
                selectFragment(Home())
            }

            is Screen.Settings -> {
                selectFragment(Settings())
            }

            is Screen.Map -> {
                selectFragment(Map())
            }

            is Screen.SignIn -> {
                selectFragment(SignIn())
            }
            else -> return
        }
    }

}
