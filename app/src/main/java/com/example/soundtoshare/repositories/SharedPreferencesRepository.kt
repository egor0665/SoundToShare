package com.example.soundtoshare.repositories

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.example.soundtoshare.external.SharedPreferencesExternal

class SharedPreferencesRepository(val sharedPreferences: SharedPreferencesExternal){

    fun init(initCallBack: Boolean.() -> Unit){
        sharedPreferences.getIncognitoMode {
            initCallBack(this)
            Log.d("SharedPreference","PreferencesInitialised2")
        }
    }

    fun setIncognitoMode(mode: Boolean){
        sharedPreferences.setIncognitoMode(mode)
    }
}