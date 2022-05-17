package com.example.soundtoshare.fragments.settings

import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.example.soundtoshare.repositories.SharedPreferencesRepository

class SharedPreferenceUseCase(val sharedPreferencesRepository: SharedPreferencesRepository ) {

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