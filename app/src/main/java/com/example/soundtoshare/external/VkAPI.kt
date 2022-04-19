package com.example.soundtoshare.external

import android.util.Log
import com.vk.api.sdk.VK
import com.vk.api.sdk.VKApiCallback
import com.vk.sdk.api.audio.dto.AudioAudio
import com.vk.sdk.api.status.StatusService
import com.vk.sdk.api.status.dto.StatusStatus
import com.vk.sdk.api.users.UsersService
import com.vk.sdk.api.users.dto.UsersFields
import com.vk.sdk.api.users.dto.UsersUserFull

object VkAPI {
    fun fetchVkMusic(fetchVkMusicCallback: AudioAudio?.() -> Unit) {
        VK.execute(
            StatusService().statusGet(VK.getUserId()),
            object : VKApiCallback<StatusStatus> {
                override fun success(result: StatusStatus) {
                    fetchVkMusicCallback(result.audio)
                }
                override fun fail(error: Exception) {
                    Log.e("error", error.toString())
                }
            }
        )
    }

    fun getUserInfoRepository(getUserInfoRepositoryCallBack: ArrayList<String>.() -> Unit) {
        VK.execute(UsersService().usersGet(arrayListOf(VK.getUserId()) ,arrayListOf(UsersFields.PHOTO_200)), object : VKApiCallback<List<UsersUserFull>> {
            override fun success(result: List<UsersUserFull>) {
                getUserInfoRepositoryCallBack(arrayListOf(result[0].photo200.toString(),result[0].lastName.toString(), result[0].firstName.toString()))
            }
                override fun fail(error: Exception) {
                    Log.e("error", error.toString())
                }
            }
        )
    }
}
