package com.example.soundtoshare.fragments.home

import androidx.activity.result.ActivityResultLauncher
import androidx.lifecycle.ViewModel
import com.vk.api.sdk.auth.VKScope

class HomeFragmentViewModel: ViewModel() {
    fun signInVK(authLauncher: ActivityResultLauncher<Collection<VKScope>>, arrayList: ArrayList<VKScope>) {
        authLauncher.launch(arrayList)
    }
    // Add Data Here
}
    // Add Functions Here

