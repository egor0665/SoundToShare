package com.example.soundtoshare.fragments.home

import androidx.lifecycle.MutableLiveData
import com.example.soundtoshare.repositories.CacheRepository
import com.example.soundtoshare.repositories.UserInfo
import com.example.soundtoshare.repositories.VkAPIRepository
import com.vk.sdk.api.audio.dto.AudioAudio

class VkGetDataUseCase(private val vkApiRepository : VkAPIRepository, private val cacheRepository: CacheRepository) {

    fun loadUserInfo(loadUserInfoCallBack: UserInfo.() -> Unit) {
        vkApiRepository.getUserInfoRepository {
            cacheRepository.setUserInfo(this!!)
            loadUserInfoCallBack(this!!)
        }
    }

    fun fetchVkMusic(fetchVkMusicCallback: AudioAudio?.() -> Unit) {
        vkApiRepository.fetchVkMusic{
            fetchVkMusicCallback(this)
        }
    }
}