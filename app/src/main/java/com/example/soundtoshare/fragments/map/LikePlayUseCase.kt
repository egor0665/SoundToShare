package com.example.soundtoshare.fragments.map

import android.util.Log
import com.example.soundtoshare.external.FireBaseDatabase
import com.example.soundtoshare.repositories.CacheRepository
import com.example.soundtoshare.repositories.User
import com.example.soundtoshare.repositories.roomdb.RoomDBRepository

class LikePlayUseCase(
    val roomDBRepository: RoomDBRepository,
    val fireBaseDatabase: FireBaseDatabase,
    val cacheRepository: CacheRepository
) {
    fun likeSong(toUser: User) {

        roomDBRepository.checkForLike(toUser) {
            Log.d("room", this.toString())
            if (this.isEmpty()) {
                fireBaseDatabase.likeSong(toUser, cacheRepository.getUserInfo())
                roomDBRepository.addLikedSong(toUser)
            }
        }
    }
}
