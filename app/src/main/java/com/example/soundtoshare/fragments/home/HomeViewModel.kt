package com.example.soundtoshare.fragments.home

import android.util.Log
import androidx.activity.result.ActivityResultLauncher
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.soundtoshare.repositories.Reaction
import com.example.soundtoshare.repositories.UserInfo
import com.vk.api.sdk.VK
import com.vk.api.sdk.auth.VKScope
import com.vk.sdk.api.audio.dto.AudioAudio
import java.util.*
import java.util.concurrent.TimeUnit.*

class HomeViewModel(val vkGetDataUseCase : VkGetDataUseCase) : ViewModel() {

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
        vkGetDataUseCase.loadUserInfo()
        firebaseGetDataUseCase.getReactions(VK.getUserId().toString()) {
            reactions.value?.add(this) ?: Log.d("firebase", "cannot add item")
            reactions.postValue(reactions.value)
            reactions.value?.forEach { Log.d("firebase", it.toString()) } ?: Log.d(
                "firebase",
                "ya hz"
            )
        }

    }

    fun getObservableReactions(): MutableLiveData<MutableList<Reaction>> {
        return reactions
    }

    fun fetchVkMusicViewModel(fetchVkMusicCallback: () -> Unit) =
        vkGetDataUseCase.fetchVkMusicUseCase{
            fetchVkMusicCallback()
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

    fun getTimeOfReaction(date : Date) : String? {
        var timeOfReaction: String? = null
        val dateDiff = Date().time - date.time

        val second: Long = MILLISECONDS.toSeconds(dateDiff)
        val minute: Long = MILLISECONDS.toMinutes(dateDiff)
        val hour: Long = MILLISECONDS.toHours(dateDiff)
        val day: Long = MILLISECONDS.toDays(dateDiff)

        when {
            second < 60 -> {
                timeOfReaction = "$second Seconds ago"
            }
            minute < 60 -> {
                timeOfReaction  = "$minute Minutes ago"
            }
            hour < 24 -> {
                timeOfReaction  = "$hour Hours ago"
            }
            day >= 7 -> {
                timeOfReaction = when {
                    day > 365 -> {
                        (day / 365).toString() + " Years ago"
                    }
                    day > 30 -> {
                        (day / 30).toString() + " Months ago"
                    }
                    else -> {
                        (day / 7).toString() + " Week ago"
                    }
                }
            }
            day < 7 -> {
                timeOfReaction = "$day Days ago"
            }
        }

        if (timeOfReaction != null) {
            Log.d("Time:", timeOfReaction)
        }
        return timeOfReaction

    }

}
