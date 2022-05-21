package com.example.soundtoshare.fragments.map

import com.example.soundtoshare.external.FireBaseDatabase
import com.example.soundtoshare.repositories.roomdb.LikedSong
import com.example.soundtoshare.repositories.roomdb.RoomDBRepository

class LikePlayUseCase(val roomDBRepository: RoomDBRepository, val fireBaseDatabase: FireBaseDatabase) {
    fun likeSong(from: String, fromId: Int,song: String, artist:String, avatar: String) {
        roomDBRepository.addLikedSong(song,artist)
//        fireBaseDatabase.likeSong(from, fromId, song, artist, avatar)
    }

    fun playSong(song: String, artist: String) {

    }
}