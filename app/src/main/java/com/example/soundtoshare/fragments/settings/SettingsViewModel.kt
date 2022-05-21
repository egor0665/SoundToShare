package com.example.soundtoshare.fragments.settings

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.soundtoshare.fragments.home.VkGetDataUseCase
import com.vk.sdk.api.audio.dto.AudioAudio

class SettingsViewModel(private val incognitoModeUseCase: IncognitoModeUseCase, private val vkGetDataUseCase: VkGetDataUseCase) : ViewModel() {

    fun setIncognitoMode(mode: Boolean) {
        incognitoModeUseCase.setIncognitoMode(mode)
    }

    fun getObservableIncognitoMode(): MutableLiveData<Boolean> {
        return incognitoModeUseCase.getIncognitoModeLiveData()
    }

    fun getIncognitoMode(): Boolean {
        return incognitoModeUseCase.getIncognitoMode()
    }

    fun checkUserStatus(checkUserStatusCallback: AudioAudio?.() -> Unit) {
        vkGetDataUseCase.fetchVkMusic {
            checkUserStatusCallback(this)
        }
    }

}

