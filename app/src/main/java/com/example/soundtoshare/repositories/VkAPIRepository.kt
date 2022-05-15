package com.example.soundtoshare.repositories

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.vk.api.sdk.VK
import com.vk.api.sdk.VKApiCallback
import com.vk.sdk.api.audio.dto.AudioAudio
import com.vk.sdk.api.status.StatusService
import com.vk.sdk.api.status.dto.StatusStatus
import com.vk.sdk.api.users.UsersService
import com.vk.sdk.api.users.dto.UsersFields
import com.vk.sdk.api.users.dto.UsersUserFull

class VkAPIRepository {

    private val userInfo: MutableLiveData<UserInfo> by lazy {
        MutableLiveData<UserInfo>()
    }

    private val songData: MutableLiveData<AudioAudio> by lazy {
        MutableLiveData<AudioAudio>()
    }

    fun fetchVkMusic(fetchVkMusicCallback: () -> Unit) {
        VK.execute(
            StatusService().statusGet(VK.getUserId()),
            object : VKApiCallback<StatusStatus> {
                override fun success(result: StatusStatus) {
                    // ЗАПРОС ДЛЯ ПОИСКА МУЗЫКИ: https://m.vk.com/audio?q=МАЛИНОВАЯ%20ЛАДА
                    songData.postValue(result.audio)
                    Log.d("Music", result.audio?.title.toString())
                    fetchVkMusicCallback()
                }
                override fun fail(error: Exception) {
                    songData.postValue(null)
                    Log.e("error", error.toString())
                }
            }
        )
    }

    fun getUserInfoRepository() {
        VK.execute(UsersService().usersGet(arrayListOf(VK.getUserId()) ,arrayListOf(UsersFields.PHOTO_200)), object : VKApiCallback<List<UsersUserFull>> {
            override fun success(result: List<UsersUserFull>) {
                setUserInfo(UserInfo(result[0].photo200.toString(),result[0].lastName.toString(), result[0].firstName.toString()))
            }
                override fun fail(error: Exception) {
                    Log.e("error", error.toString())
                }
            }
        )
    }

    fun setUserInfo(_userInfo: UserInfo?) {
        userInfo.postValue(_userInfo)
    }

    fun setSongData(_songData: AudioAudio?) {
        songData.postValue(_songData)
    }

    fun getUserInfoLiveData(): MutableLiveData<UserInfo> {
        return userInfo
    }

    fun getSongDataLiveData(): MutableLiveData<AudioAudio> {
        return songData
    }

    fun getUserInfo(): UserInfo? {
        return userInfo.value
    }

    fun getSongData(): AudioAudio? {
        return songData.value
    }

}
