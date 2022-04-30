package com.example.soundtoshare.repositories

import android.content.Context
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.example.soundtoshare.external.SharedPreferencesExternal

class SharedPreferencesRepository{
    private val sharedPreferences = SharedPreferencesExternal()
    private val incognitoMode: MutableLiveData<Boolean> by lazy {
        MutableLiveData<Boolean>()
    }

    fun initialize(context: Context) {
        Log.d("SharedPreference","PreferencesInitialised1")
        sharedPreferences.initialize(context)
        sharedPreferences.getIncognitoMode {
            incognitoMode.postValue(this)
            Log.d("SharedPreference","PreferencesInitialised2")
        }
    }

    fun getIncognitoMode(): Boolean {
        return (incognitoMode.value ?: false)
    }

    fun getObservableSharedPreference():MutableLiveData<Boolean>{
        return incognitoMode
    }

    fun setIncognitoMode(mode: Boolean){
        incognitoMode.postValue(mode)
        sharedPreferences.setIncognitoMode(mode)
    }

}