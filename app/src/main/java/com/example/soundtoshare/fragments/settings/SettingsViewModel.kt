package com.example.soundtoshare.fragments.settings

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.soundtoshare.fragments.home.VkGetDataUseCase
import com.vk.sdk.api.audio.dto.AudioAudio

class SettingsViewModel(private val sharedPreferenceUseCase: SharedPreferenceUseCase, private val vkGetDataUseCase: VkGetDataUseCase) : ViewModel() {

    fun setIncognitoMode(mode: Boolean) {
        sharedPreferenceUseCase.setIncognitoMode(mode)
    }

    fun getObservableIncognitoMode(): MutableLiveData<Boolean>{
        return sharedPreferenceUseCase.getObservableSharedPreference()
    }

    fun getIncognitoMode(): Boolean {
        return sharedPreferenceUseCase.getIncognitoMode()
    }

    fun checkUserStatus(checkUserStatusCallback: AudioAudio?.() -> Unit) {
        vkGetDataUseCase.fetchVkMusic {
            checkUserStatusCallback(this)
        }
    }

}

