package com.example.soundtoshare.fragments.settings

import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.example.soundtoshare.external.SharedPreferencesExternal
import com.example.soundtoshare.main.MainActivity
import com.example.soundtoshare.repositories.SharedPreferencesRepository

class SharedPreferenceUseCase(context: Context) {
    private val sharedPreferencesRepository = SharedPreferencesRepository()

    init {
        sharedPreferencesRepository.initialize(context)
    }

    fun setIncognitoModeUseCase(mode: Boolean) {
        sharedPreferencesRepository.setIncognitoMode(mode)
    }

    fun getObservableIncognitoModeSP(): MutableLiveData<Boolean>{
        return sharedPreferencesRepository.getObservableSharedPreference()
    }

    fun getIncognitoModeUseCase(): Boolean {
        return sharedPreferencesRepository.getIncognitoMode()
    }

}