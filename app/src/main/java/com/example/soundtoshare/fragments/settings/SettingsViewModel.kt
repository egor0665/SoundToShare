package com.example.soundtoshare.fragments.settings

class SettingsViewModel(private val incognitoModeUseCase: IncognitoModeUseCase, private val vkGetDataUseCase: VkGetDataUseCase) : ViewModel() {

    fun setIncognitoMode(mode: Boolean) {
        incognitoModeUseCase.setIncognitoMode(mode)
    }

    fun getObservableIncognitoMode(): MutableLiveData<Boolean>{
        return incognitoModeUseCase.getObservableSharedPreference()
    }

    fun getIncognitoMode(): Boolean {
        return incognitoModeUseCase.getIncognitoMode()
    }

    fun checkUserStatus(checkUserStatusCallback: AudioAudio?.() -> Unit) {
        vkGetDataUseCase.fetchVkMusic {
            checkUserStatusCallback(this)
        }
    }

    fun checkUserStatus(checkUserStatusCallback: AudioAudio?.() -> Unit) {
        vkGetDataUseCase.fetchVkMusicUseCase {
            checkUserStatusCallback(this)
        }
    }

}

