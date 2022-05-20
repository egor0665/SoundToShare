package com.example.soundtoshare.fragments.settings

import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.example.soundtoshare.repositories.SharedPreferencesRepository

class IncognitoModeUseCase(val sharedPreferencesRepository: SharedPreferencesRepository ) {
    private val incognitoMode: MutableLiveData<Boolean> by lazy {
        MutableLiveData<Boolean>()
    }

    init{
        sharedPreferencesRepository.init {
            incognitoMode.postValue(this)
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
        sharedPreferencesRepository.setIncognitoMode(mode)
    }
}