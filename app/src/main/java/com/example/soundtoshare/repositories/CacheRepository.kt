package com.example.soundtoshare.repositories

import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.vk.sdk.api.audio.dto.AudioAudio

class CacheRepository() {
    private val userInfo: MutableLiveData<UserInfo> by lazy {
        MutableLiveData<UserInfo>()
    }
    private val songData: MutableLiveData<AudioAudio> by lazy {
        MutableLiveData<AudioAudio>()
    }
    private val incognitoMode: MutableLiveData<Boolean> by lazy {
        MutableLiveData<Boolean>()
    }

    fun setUserInfo(userInfo: UserInfo){
        this.userInfo.postValue(userInfo)
    }

    fun songData(songData: AudioAudio){
        this.songData.postValue(songData)
    }

    fun setIncognitoMode(mode: Boolean){
        this.incognitoMode.postValue(mode)
    }

    fun getUserInfo() : UserInfo {
        return this.userInfo.value!!
    }

    fun getSongData() : AudioAudio? {
        return this.songData.value
    }

    fun getIncognitoMode() : Boolean {
        return (this.incognitoMode.value ?: false)
    }

    fun getUserInfoLiveData() : MutableLiveData<UserInfo>{
        return userInfo
    }

    fun getSongDataLiveData() : MutableLiveData<AudioAudio>{
        return songData
    }

    fun getIncognitoModeLiveData() : MutableLiveData<Boolean>{
        return incognitoMode
    }

}