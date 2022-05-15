package com.example.soundtoshare.fragments.home

import androidx.lifecycle.MutableLiveData
import com.example.soundtoshare.external.FirestoreDatabase
import com.example.soundtoshare.repositories.UserInfo
import com.example.soundtoshare.repositories.VkAPIRepository
import com.vk.sdk.api.audio.dto.AudioAudio

class VkGetDataUseCase(private val vkApiRepository : VkAPIRepository) {
//    private val vkApi = VkAPIRepository
    fun loadUserInfo() {
        vkApiRepository.getUserInfoRepository()
    }

    fun fetchVkMusicUseCase(fetchVkMusicCallback: () -> Unit) {
       vkApiRepository.fetchVkMusic{
           fetchVkMusicCallback()
       }
    }

    fun setUserInfo(_userInfo: UserInfo?) {
        vkApiRepository.setUserInfo(_userInfo)
    }

    fun setSongData(_songData: AudioAudio?) {
        vkApiRepository.setSongData(_songData)
    }

    fun getUserInfoLiveData(): MutableLiveData<UserInfo> {
        return vkApiRepository.getUserInfoLiveData()
    }

    fun getSongDataLiveData(): MutableLiveData<AudioAudio> {
        return vkApiRepository.getSongDataLiveData()
    }

    fun getUserInfo(): UserInfo? {
        return vkApiRepository.getUserInfo()
    }
}

