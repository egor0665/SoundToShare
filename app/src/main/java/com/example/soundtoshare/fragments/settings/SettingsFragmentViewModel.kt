package com.example.soundtoshare.fragments.settings

import android.content.Context
import androidx.lifecycle.ViewModel
import com.example.soundtoshare.SharedPreferenceUseCase
import com.example.soundtoshare.external.FullUserData

class SettingsFragmentViewModel(context: Context) : ViewModel() {
    private val sharedPreferenceUseCase = SharedPreferenceUseCase(context)

    init {
        sharedPreferenceUseCase.getIncognitoModeUseCase {
            FullUserData.incognitoMode.postValue(this)
        }
    }

    fun setIncognitoMode(mode: Boolean) {
        sharedPreferenceUseCase.setIncognitoModeUseCase(mode)
    }
}

