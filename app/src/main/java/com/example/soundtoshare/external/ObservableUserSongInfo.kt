package com.example.soundtoshare.external

import android.graphics.Bitmap
import androidx.lifecycle.MutableLiveData
import com.google.firebase.firestore.auth.User
import com.vk.sdk.api.audio.dto.AudioAudio

object ObservableUserSongInfo {
    private val userInfo: MutableLiveData<UserInfo> by lazy {
        MutableLiveData<UserInfo>()
    }

    private val songData: MutableLiveData<AudioAudio> by lazy {
        MutableLiveData<AudioAudio>()
    }

    fun setUserInfo(_userInfo: UserInfo?) {
        userInfo.postValue(_userInfo)
    }

//    fun setIncognitoMode(_incognitoMode: Boolean?) {
//        incognitoMode.postValue(_incognitoMode)
//    }

    fun setSongData(_songData: AudioAudio?) {
        songData.postValue(_songData)
    }

    fun getUserInfoLiveData(): MutableLiveData<UserInfo> {
        return userInfo
    }

//    fun getIncognitoModeLiveData(): MutableLiveData<Boolean> {
//        return incognitoMode
//    }

    fun getSongDataLiveData(): MutableLiveData<AudioAudio> {
        return songData
    }

    fun getUserInfo(): UserInfo? {
        return userInfo.value
    }

//    fun getIncognitoMode(): Boolean? {
//        return incognitoMode.value
//    }

    fun getSongData(): AudioAudio? {
        return songData.value
    }
}

data class UserInfo(val avatar: Bitmap, val lastName: String, val firstName: String)
