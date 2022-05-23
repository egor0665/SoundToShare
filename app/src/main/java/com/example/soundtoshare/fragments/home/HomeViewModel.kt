package com.example.soundtoshare.fragments.home

import android.util.Log
import androidx.activity.result.ActivityResultLauncher
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.soundtoshare.repositories.Reaction
import com.example.soundtoshare.repositories.UserInfo
import com.example.soundtoshare.repositories.roomdb.LikedSong
import com.vk.api.sdk.VK
import com.vk.api.sdk.auth.VKScope
import java.util.*

class HomeViewModel(val vkGetDataUseCase : VkGetDataUseCase, val likedSongsUseCase: LikedSongsUseCase) : ViewModel() {
    private val firebaseGetDataUseCase = FireBaseGetDataUseCase()
    private val reactions : MutableLiveData<MutableList<Reaction>> by lazy {
        MutableLiveData<MutableList<Reaction>>()
    }
    private val likedSongs : MutableLiveData<MutableList<LikedSong>> by lazy {
        MutableLiveData<MutableList<LikedSong>>()
    }
    private val userInfo: MutableLiveData<UserInfo> by lazy {
        MutableLiveData<UserInfo>()
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

    fun loadLikedSongs(){
        likedSongsUseCase.getLikedSongs { likedSongs.postValue(this) }
    }

    fun loadUserInfo(loadUserInfoCallBack : () -> Unit) {
        Log.d("test","KoinViewModel")
        vkGetDataUseCase.loadUserInfo(){
            userInfo.postValue(this)
        }
        firebaseGetDataUseCase.getReactions(VK.getUserId().toString()) {
            reactions.value?.add(this) ?: Log.d("firebase", "cannot add item")
            reactions.postValue(reactions.value)
            reactions.value?.forEach {
                Log.d("firebase", it.toString())
            } ?: Log.d(
                "firebase",
                "ya hz"
            )
            loadUserInfoCallBack()
        }

    }

    fun getObservableReactions(): MutableLiveData<MutableList<Reaction>> {
        return reactions
    }

    fun getObservableLikedSongs(): MutableLiveData<MutableList<LikedSong>> {
        return likedSongs
    }

    fun fetchVkMusic(fetchVkMusicCallback: () -> Unit) {
        vkGetDataUseCase.fetchVkMusic {
            fetchVkMusicCallback()
        }
    }

    fun getUserInfoLiveData(): MutableLiveData<UserInfo> {
        return userInfo
    }

    fun addLikedSong() {
        likedSongsUseCase.addLikedSong()
    }
}
