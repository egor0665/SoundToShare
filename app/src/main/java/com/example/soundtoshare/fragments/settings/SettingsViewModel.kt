package com.example.soundtoshare.fragments.settings

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.soundtoshare.repositories.SharedPreferencesRepository

class SettingsViewModel(context: Context) : ViewModel() {
    private val sharedPreferenceUseCase = SharedPreferenceUseCase(context)

    fun setIncognitoMode(mode: Boolean) {
        sharedPreferenceUseCase.setIncognitoModeUseCase(mode)
    }

    fun getObservableIncognitoMode(): MutableLiveData<Boolean>{
        return sharedPreferenceUseCase.getObservableIncognitoModeSP()
    }

    fun getIncognitoMode(): Boolean {
        return sharedPreferenceUseCase.getIncognitoModeUseCase()
    }

}

