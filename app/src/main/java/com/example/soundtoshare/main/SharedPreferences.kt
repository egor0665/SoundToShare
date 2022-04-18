package com.example.soundtoshare.main

import android.content.Context

class SharedPreferences(context: Context) {

    private val SHARED_PREFERENCE_NAME = "SoundToShareSP"
    private val INCOGNITO_MODE = "IncognitoMode"
    val preferences = context.getSharedPreferences(SHARED_PREFERENCE_NAME, Context.MODE_PRIVATE)

    fun getIncognitoMode(): Boolean {
        return preferences.getBoolean(INCOGNITO_MODE, false)
    }
    fun setIncognitoMode(mode: Boolean) {
        val editor = preferences.edit()
        editor.putBoolean(INCOGNITO_MODE,mode)
        editor.apply()
    }
}