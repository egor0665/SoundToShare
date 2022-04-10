package com.example.soundtoshare.fragments.home

import android.graphics.Bitmap
import androidx.activity.result.ActivityResultLauncher
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.vk.api.sdk.auth.VKScope


class HomeFragmentViewModel: ViewModel() {
    private val vkGetDataUseCase = VkGetDataUseCase()
    val bitmapAvatar: MutableLiveData<Bitmap> by lazy {
        MutableLiveData<Bitmap>()
    }

    fun signInVK(authLauncher: ActivityResultLauncher<Collection<VKScope>>,  arrayList: ArrayList<VKScope>) {
        authLauncher.launch(arrayList)
    }

    fun initViewModel() {
        vkGetDataUseCase.getUserInfo {
            bitmapAvatar.postValue(this)
        }
    }
}