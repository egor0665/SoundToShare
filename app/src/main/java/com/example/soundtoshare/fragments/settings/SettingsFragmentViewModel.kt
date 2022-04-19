package com.example.soundtoshare.fragments.settings

import android.content.Context
import androidx.lifecycle.ViewModel
import com.example.soundtoshare.SharedPreferenceUseCase

class SettingsFragmentViewModel(context: Context) : ViewModel() {
    private val sharedPreferenceUseCase = SharedPreferenceUseCase(context)
    fun getIncognitoMode():Boolean{
        return sharedPreferenceUseCase.getIncognitoModeUseCase()
    }

    fun setIncognitoMode(mode: Boolean) {
        sharedPreferenceUseCase.setIncognitoModeUseCase(mode)
    }
}

