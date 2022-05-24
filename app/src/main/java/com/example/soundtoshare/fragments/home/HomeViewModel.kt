package com.example.soundtoshare.fragments.home

import android.util.Log
import androidx.activity.result.ActivityResultLauncher
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.soundtoshare.fragments.map.LocationUpdateUseCase
import com.example.soundtoshare.repositories.Reaction
import com.example.soundtoshare.repositories.UserInfo
import com.example.soundtoshare.repositories.roomdb.LikedSong
import com.vk.api.sdk.VK
import com.vk.api.sdk.auth.VKScope
import com.vk.sdk.api.audio.dto.AudioAudio
import java.util.*

class HomeViewModel(val vkGetDataUseCase : VkGetDataUseCase, val likedSongsUseCase: LikedSongsUseCase, val locationUpdateUseCase: LocationUpdateUseCase) : ViewModel() {
    private val firebaseGetDataUseCase = FireBaseGetDataUseCase()
    private var checkReactionsEmpty = false
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
//        reactions.postValue(mutableListOf())
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
            if (!checkReactionsEmpty) {
                reactions.value = mutableListOf()
                checkReactionsEmpty = true
            }
            if (this != null) {
                reactions.value?.add(this) ?: Log.d("firebase", "cannot add item")
                reactions.postValue(reactions.value)
                reactions.value?.forEach {
                    Log.d("firebase", it.toString())
                } ?: Log.d(
                    "firebase",
                    "ya hz"
                )
            }
            else {
                reactions.postValue(mutableListOf())
            }
            loadUserInfoCallBack()

        }

    }

    fun getObservableReactions(): MutableLiveData<MutableList<Reaction>> {
        return reactions
    }

    fun getObservableLikedSongs(): MutableLiveData<MutableList<LikedSong>> {
        return likedSongs
    }

    fun fetchVkMusic(fetchVkMusicCallback: AudioAudio?.() -> Unit) {
        vkGetDataUseCase.fetchVkMusic {
            fetchVkMusicCallback(this)
        }
    }

    fun getUserInfoLiveData(): MutableLiveData<UserInfo> {
        return userInfo
    }

    fun addLikedSong() {
        likedSongsUseCase.addLikedSong()
    }
}
