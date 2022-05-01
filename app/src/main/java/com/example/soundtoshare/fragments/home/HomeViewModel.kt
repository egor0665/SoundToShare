package com.example.soundtoshare.fragments.home

import android.util.Log
import androidx.activity.result.ActivityResultLauncher
import androidx.lifecycle.ViewModel
import com.example.soundtoshare.external.ObservableUserSongInfo
import com.vk.api.sdk.auth.VKScope

class HomeViewModel(val vkGetDataUseCase : VkGetDataUseCase) : ViewModel() {
//    private val vkGetDataUseCase = VkGetDataUseCase()

    init{
        Log.d("ViewModel", "Created ViewModel")
    }
    fun signInVK(
        authLauncher: ActivityResultLauncher<Collection<VKScope>>,
        arrayList: ArrayList<VKScope>
    ) {
        authLauncher.launch(arrayList)
    }

    fun getUserInfo() {
        Log.d("test","KoinViewModel")
        vkGetDataUseCase.getUserInfo {
            ObservableUserSongInfo.setUserInfo(this)
        }
    }


}
