package com.example.soundtoshare.external

import android.content.Context
import android.content.SharedPreferences
import com.example.soundtoshare.repositories.SharedPreferencesRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SharedPreferencesExternal {
    private lateinit var preferences: SharedPreferences

    fun initialize(context: Context) {
        preferences = context.getSharedPreferences(sharedPreferenceName, Context.MODE_PRIVATE)!!
    }

    fun getIncognitoMode( getIncognitoModeCallback: Boolean.() -> Unit){
        CoroutineScope(Dispatchers.IO).launch {
            getIncognitoModeCallback(preferences.getBoolean(incognitoModeString, false))
        }
    }

    fun setIncognitoMode(mode: Boolean) {
        CoroutineScope(Dispatchers.IO).launch {
            val editor = preferences.edit()
            editor.putBoolean(incognitoModeString,mode)
            editor.apply()
        }
    }

    companion object {
        const val sharedPreferenceName = "SoundToShareSP"
        const val incognitoModeString = "IncognitoMode"
    }
}