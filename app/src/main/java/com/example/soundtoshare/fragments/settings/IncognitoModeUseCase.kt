package com.example.soundtoshare.fragments.settings

import androidx.lifecycle.MutableLiveData
import com.example.soundtoshare.repositories.CacheRepository
import com.example.soundtoshare.repositories.SharedPreferencesRepository

class IncognitoModeUseCase(
    val sharedPreferencesRepository: SharedPreferencesRepository,
    private val cacheRepository: CacheRepository
) {
    init {
        sharedPreferencesRepository.init {
            cacheRepository.setIncognitoMode(this)
        }
    }

    fun getIncognitoMode(): Boolean {
        return cacheRepository.getIncognitoMode()
    }

    fun getIncognitoModeLiveData(): MutableLiveData<Boolean> {
        return cacheRepository.getIncognitoModeLiveData()
    }

    fun setIncognitoMode(mode: Boolean) {
        cacheRepository.setIncognitoMode(mode)
        sharedPreferencesRepository.setIncognitoMode(mode)
    }
}
