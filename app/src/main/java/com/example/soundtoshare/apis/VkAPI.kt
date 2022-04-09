package com.example.soundtoshare.apis

import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import com.example.soundtoshare.ImageManager
import com.vk.api.sdk.VK
import com.vk.api.sdk.VKApiCallback
import com.vk.api.sdk.requests.VKRequest
import com.vk.sdk.api.audio.dto.AudioAudio
import com.vk.sdk.api.base.dto.BaseUserGroupFields
import com.vk.sdk.api.status.StatusService
import com.vk.sdk.api.status.dto.StatusStatus
import com.vk.sdk.api.users.UsersService
import com.vk.sdk.api.users.dto.UsersFields
import com.vk.sdk.api.users.dto.UsersUserFull
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONException
import org.json.JSONObject
import java.net.URL
import javax.net.ssl.HttpsURLConnection

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
                ImageManager.fetchImage(result[0].photo200,iView)
            }

            override fun fail(error: Exception) {
                Log.e("error", error.toString())
            }
        })
    }
}