package com.example.soundtoshare.repositories

import android.content.Context

class SharedPreferencesRepository(context: Context) {
    private val preferences = context.getSharedPreferences(
        sharedPreferenceName,
        Context.MODE_PRIVATE
    )!!

    fun init(initCallBack: Boolean.() -> Unit) {
        getIncognitoMode {
            initCallBack(this)
        }
    }
    fun setIncognitoMode(mode: Boolean) {
        val editor = preferences.edit()
        editor.putBoolean(incognitoModeString, mode)
        editor.apply()
    }

    private fun getIncognitoMode(getIncognitoModeCallback: Boolean.() -> Unit) {
        getIncognitoModeCallback(preferences.getBoolean(incognitoModeString, false))
    }

    companion object {
        const val sharedPreferenceName = "SoundToShareSP"
        const val incognitoModeString = "IncognitoMode"
    }
}
