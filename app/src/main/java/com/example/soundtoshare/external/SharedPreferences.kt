package com.example.soundtoshare.external

import android.content.Context
import android.content.SharedPreferences

class SharedPreferences(context: Context) {
    private val preferences: SharedPreferences = context.getSharedPreferences(sharedPreferenceName, Context.MODE_PRIVATE)!!

    fun getIncognitoMode(): Boolean {
        return preferences.getBoolean(incognitoMode, false)
    }
    fun setIncognitoMode(mode: Boolean) {
        val editor = preferences.edit()
        editor.putBoolean(incognitoMode,mode)
        editor.apply()
    }

    companion object {
        const val sharedPreferenceName = "SoundToShareSP"
        const val incognitoMode = "IncognitoMode"
    }
}