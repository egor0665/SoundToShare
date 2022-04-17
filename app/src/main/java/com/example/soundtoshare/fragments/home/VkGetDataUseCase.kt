package com.example.soundtoshare.fragments.home

import android.graphics.Bitmap
import com.example.soundtoshare.apis.Network
import com.example.soundtoshare.apis.VkAPI

class VkGetDataUseCase {
    private val vkApi = VkAPI
    private val network = Network()
    fun getUserInfo(getUserInfoCallback : UserInfo.() -> Unit) {
        vkApi.getUserInfoRepository {
            val listUserInfo = this
            loadAvatar (listUserInfo[0]){
               getUserInfoCallback(UserInfo(this,listUserInfo[1],listUserInfo[2]))
            }
        }
    }

    private fun loadAvatar(iUrl : String, loadAvatarCallback: Bitmap.() -> Unit){
        network.loadAvatar(iUrl){
            loadAvatarCallback(this)
        }
    }
}

data class UserInfo(val avatar : Bitmap, val lastName : String, val firstName : String)