package com.example.soundtoshare.external

import android.content.Context

class SharedPreferencesExternal(context: Context) {
    private val preferences = context.getSharedPreferences(sharedPreferenceName, Context.MODE_PRIVATE)!!

    fun getIncognitoMode( getIncognitoModeCallback: Boolean.() -> Unit){
        getIncognitoModeCallback(preferences.getBoolean(incognitoModeString, false))
    }

    fun setIncognitoMode(mode: Boolean) {
        val editor = preferences.edit()
        editor.putBoolean(incognitoModeString,mode)
        editor.apply()
    }

    companion object {
        const val sharedPreferenceName = "SoundToShareSP"
        const val incognitoModeString = "IncognitoMode"
    }
}