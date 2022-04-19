package com.example.soundtoshare

import android.content.Context
import com.example.soundtoshare.external.SharedPreferences

class SharedPreferenceUseCase(context: Context) {
    private var incognitoMode: Boolean = false
    private var incognitoModeLoaded: Boolean = false
    private val sharedPreferences = SharedPreferences(context)

    fun getIncognitoModeUseCase(): Boolean {
        if (!incognitoModeLoaded)
            incognitoMode = sharedPreferences.getIncognitoMode()
        return incognitoMode
    }

    fun setIncognitoModeUseCase(mode: Boolean) {
        incognitoMode = mode
        sharedPreferences.setIncognitoMode(mode)
    }
}