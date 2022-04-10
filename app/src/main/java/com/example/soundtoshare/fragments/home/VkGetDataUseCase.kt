package com.example.soundtoshare.fragments.home

import android.graphics.Bitmap
import com.example.soundtoshare.apis.Network
import com.example.soundtoshare.apis.VkAPI

class VkGetDataUseCase {
    private val vkApi = VkAPI
    private val network = Network()
    fun getUserInfo(getUserInfoCallback : Bitmap.() -> Unit) {
        vkApi.getUserInfoRepository {
            loadAvatar (this){
                getUserInfoCallback(this)
            }
        }
    }

    private fun loadAvatar(iUrl : String, loadAvatarCallback: Bitmap.() -> Unit){
        network.loadAvatar(iUrl){
            loadAvatarCallback(this)
        }
    }
}