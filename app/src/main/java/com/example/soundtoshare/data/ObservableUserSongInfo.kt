package com.example.soundtoshare.external

import android.graphics.Bitmap
import androidx.lifecycle.MutableLiveData
import com.vk.sdk.api.audio.dto.AudioAudio

object FullUserData {
    val userInfo: MutableLiveData<UserInfo> by lazy {
        MutableLiveData<UserInfo>()
    }
    val incognitoMode: MutableLiveData<Boolean> by lazy {
        MutableLiveData<Boolean>()
    }
    val songData: MutableLiveData<AudioAudio> by lazy {
        MutableLiveData<AudioAudio>()
    }
}
data class UserInfo(val avatar : Bitmap, val lastName : String, val firstName : String)
