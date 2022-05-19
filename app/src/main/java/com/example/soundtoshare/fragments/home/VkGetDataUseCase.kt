package com.example.soundtoshare.fragments.home

import androidx.lifecycle.MutableLiveData
import com.example.soundtoshare.repositories.UserInfo
import com.example.soundtoshare.repositories.VkAPIRepository
import com.vk.sdk.api.audio.dto.AudioAudio

class VkGetDataUseCase(private val vkApiRepository : VkAPIRepository) {
    private val userInfo: MutableLiveData<UserInfo> by lazy {
        MutableLiveData<UserInfo>()
    }

    private val songData: MutableLiveData<AudioAudio> by lazy {
        MutableLiveData<AudioAudio>()
    }

    fun loadUserInfo(loadUserInfoCallBack: UserInfo.() -> Unit) {
        vkApiRepository.getUserInfoRepository {
                userInfo.postValue(this)
                loadUserInfoCallBack(this!!)
        }
    }

    fun fetchVkMusic(fetchVkMusicCallback: AudioAudio?.() -> Unit) {
       vkApiRepository.fetchVkMusic{
           fetchVkMusicCallback()
       }
    }

    fun getUserInfo() : UserInfo {
        return userInfo.value!!
    }

    fun getUserInfoLiveData() : MutableLiveData<UserInfo> {
        return userInfo
    }

    fun getSongData(): AudioAudio? {
        return songData.value
    }

    fun getSongDataLiveData(): MutableLiveData<AudioAudio> {
        return songData
    }

}

