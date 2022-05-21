package com.example.soundtoshare.fragments.home

import com.example.soundtoshare.repositories.roomdb.LikedSong
import com.example.soundtoshare.repositories.roomdb.RoomDBRepository

class LikedSongsUseCase(val roomDBRepository: RoomDBRepository){
    fun getLikedSongs(getLikedSongsCallback: MutableList<LikedSong>.() -> Unit){
        roomDBRepository.getLikedSongs{
            getLikedSongsCallback(this)
        }
    }

    fun addLikedSong() {
//        roomDBRepository.addLikedSong()
    }
}