package com.example.soundtoshare.fragments.home

import android.graphics.*
import androidx.lifecycle.MutableLiveData
import com.example.soundtoshare.external.Network
import com.example.soundtoshare.repositories.UserInfo
import com.example.soundtoshare.repositories.VkAPIRepository
import com.vk.sdk.api.audio.dto.AudioAudio

class VkGetDataUseCase(private val vkApiRepository : VkAPIRepository) {
//    private val vkApi = VkAPIRepository
    private val network = Network()
    fun loadUserInfo() {
        vkApiRepository.getUserInfoRepository {
            val listUserInfo = this
            loadAvatar (listUserInfo[0]){
                val avatar = this
                val output = Bitmap.createBitmap(avatar.width, avatar.height, Bitmap.Config.ARGB_8888)
                val canvas = Canvas(output)
                val paint = Paint()
                val rect = Rect(0, 0, avatar.width, avatar.height)
                val rectF = RectF(Rect(0, 0, avatar.width, avatar.height))
                paint.isAntiAlias = true
                canvas.drawARGB(0, 0, 0, 0)
                paint.color = -0xbdbdbe
                canvas.drawRoundRect(
                    rectF,
                    defaultImageSize,
                    defaultImageSize,
                    paint
                )
                paint.xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_IN)
                canvas.drawBitmap(avatar, rect, rect, paint)
               vkApiRepository.setUserInfo(UserInfo(output,listUserInfo[1],listUserInfo[2]))
            }
        }
    }

    private fun loadAvatar(iUrl: String, loadAvatarCallback: Bitmap.() -> Unit) {
        network.loadAvatar(iUrl) {
            loadAvatarCallback(this)
        }
    }

    fun fetchVkMusicUseCase(fetchVkMusicCallback: AudioAudio?.() -> Unit) {
       vkApiRepository.fetchVkMusic{
           fetchVkMusicCallback(this)
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
    companion object {
        const val defaultImageSize = 100f
    }
}

