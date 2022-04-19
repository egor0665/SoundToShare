package com.example.soundtoshare.fragments.home

import androidx.activity.result.ActivityResultLauncher
import androidx.lifecycle.ViewModel
import com.example.soundtoshare.external.ObservableUserSongInfo
import com.vk.api.sdk.auth.VKScope

class HomeFragmentViewModel : ViewModel() {
    private val vkGetDataUseCase = VkGetDataUseCase()


    fun signInVK(
        authLauncher: ActivityResultLauncher<Collection<VKScope>>,
        arrayList: ArrayList<VKScope>
    ) {
        authLauncher.launch(arrayList)
    }

    init {
        vkGetDataUseCase.getUserInfo {
            ObservableUserSongInfo.userInfo.postValue(this)
        }
    }

}
