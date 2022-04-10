package com.example.soundtoshare.apis

import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import com.vk.api.sdk.VK
import com.vk.api.sdk.VKApiCallback
import com.vk.sdk.api.audio.dto.AudioAudio
import com.vk.sdk.api.status.StatusService
import com.vk.sdk.api.status.dto.StatusStatus
import com.vk.sdk.api.users.UsersService
import com.vk.sdk.api.users.dto.UsersFields
import com.vk.sdk.api.users.dto.UsersUserFull
import java.net.URL

object VkAPI {
    var audio: AudioAudio? = null

    fun fetchVkMusic(): AudioAudio? {
        VK.execute(StatusService().statusGet(VK.getUserId()), object : VKApiCallback<StatusStatus> {
            override fun success(result: StatusStatus) {
                audio = result.audio
            }

            override fun fail(error: Exception) {
                Log.e("error", error.toString())
            }
        })
        return audio
    }

    fun getAudioURL(audio: AudioAudio): String {
        return "https://vk.com/audio" + audio.ownerId + "_" + audio.id
    }

    fun getUserInfo(iView : ImageView, firstName : TextView, lastName : TextView) {
        VK.execute(UsersService().usersGet(arrayListOf(VK.getUserId()) ,arrayListOf(UsersFields.PHOTO_200)), object : VKApiCallback<List<UsersUserFull>> {
            override fun success(result: List<UsersUserFull>) {
                firstName.text = result[0].firstName
                lastName.text = result[0].lastName
                result[0].photo200?.let { ImageManager.fetchImage(URL(it),iView) }
            }

            override fun fail(error: Exception) {
                Log.e("error", error.toString())
            }
        })
    }
}