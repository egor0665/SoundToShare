package com.example.soundtoshare.fragments.settings

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.soundtoshare.fragments.home.VkGetDataUseCase
import com.vk.sdk.api.audio.dto.AudioAudio

class SettingsViewModel(context: Context, private val sharedPreferenceUseCase: SharedPreferenceUseCase, private val vkGetDataUseCase: VkGetDataUseCase) : ViewModel() {

    fun setIncognitoMode(mode: Boolean) {
        sharedPreferenceUseCase.setIncognitoModeUseCase(mode)
    }

    fun getObservableIncognitoMode(): MutableLiveData<Boolean>{
        return sharedPreferenceUseCase.getObservableIncognitoModeSP()
    }

    fun getIncognitoMode(): Boolean {
        return sharedPreferenceUseCase.getIncognitoModeUseCase()
    }

    fun checkUserStatus(checkUserStatusCallback: AudioAudio?.() -> Unit) {
        vkGetDataUseCase.fetchVkMusicUseCase {
            checkUserStatusCallback(this)
        }
    }

}

