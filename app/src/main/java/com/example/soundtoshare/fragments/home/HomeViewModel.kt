package com.example.soundtoshare.fragments.home

import android.util.Log
import androidx.activity.result.ActivityResultLauncher
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.soundtoshare.repositories.Reaction
import com.example.soundtoshare.repositories.UserInfo
import com.vk.api.sdk.auth.VKScope
import com.vk.sdk.api.audio.dto.AudioAudio

class HomeViewModel(val vkGetDataUseCase : VkGetDataUseCase) : ViewModel() {
//    private val vkGetDataUseCase = VkGetDataUseCase()
    private val firebaseGetDataUseCase = FireBaseGetDataUseCase()
    private val reactions : MutableLiveData<MutableList<Reaction>> by lazy {
        MutableLiveData<MutableList<Reaction>>()
    }
    init{
        Log.d("ViewModel", "Created ViewModel")
        reactions.postValue(mutableListOf())
    }
    fun signInVK(
        authLauncher: ActivityResultLauncher<Collection<VKScope>>,
        arrayList: ArrayList<VKScope>
    ) {
        authLauncher.launch(arrayList)
    }

    fun loadUserInfo() {
        Log.d("test","KoinViewModel")
        vkGetDataUseCase.loadUserInfo(){
            firebaseGetDataUseCase.getReactions(this){
                reactions.value?.add(this) ?: Log.d("firebase", "cannot add item")
                reactions.postValue(reactions.value)
                reactions.value?.forEach{Log.d("firebase",it.toString() )} ?: Log.d("firebase", "ya hz")
            }
        }
    }

    fun getObservableReactions(): MutableLiveData<MutableList<Reaction>> {
        return reactions
    }

    fun fetchVkMusicViewModel(fetchVkMusicCallback: AudioAudio?.() -> Unit) {
        vkGetDataUseCase.fetchVkMusicUseCase{
            fetchVkMusicCallback(this)
        }
    }

    fun setUserInfo(_userInfo: UserInfo?) {
        vkGetDataUseCase.setUserInfo(_userInfo)
    }

    fun setSongData(_songData: AudioAudio?) {
        vkGetDataUseCase.setSongData(_songData)
    }

    fun getUserInfoLiveData(): MutableLiveData<UserInfo> {
        return vkGetDataUseCase.getUserInfoLiveData()
    }

    fun getSongDataLiveData(): MutableLiveData<AudioAudio> {
        return vkGetDataUseCase.getSongDataLiveData()
    }

    fun getUserInfo(): UserInfo? {
        return vkGetDataUseCase.getUserInfo()
    }

}
