package com.example.soundtoshare

import android.content.Context
import com.example.soundtoshare.external.SharedPreferences

class SharedPreferenceUseCase(context: Context) {
    private var incognitoMode: Boolean = false
    private var incognitoModeLoaded: Boolean = false
    private val sharedPreferences = SharedPreferences(context)

    fun getIncognitoModeUseCase(getIncognitoModeUseCaseCallback : Boolean.() -> Unit) {
        if (!incognitoModeLoaded)
            sharedPreferences.getIncognitoMode {
                incognitoMode = this
                incognitoModeLoaded = true
                getIncognitoModeUseCaseCallback(this)
            }
        else
        {
            getIncognitoModeUseCaseCallback(incognitoMode)
        }
    }

    fun setIncognitoModeUseCase(mode: Boolean) {
        incognitoMode = mode
        sharedPreferences.setIncognitoMode(mode)
    }
}