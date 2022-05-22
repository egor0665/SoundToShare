package com.example.soundtoshare.repositories

import androidx.room.*
import com.example.soundtoshare.repositories.roomdb.LikedSong
import com.example.soundtoshare.repositories.roomdb.LikedSongDao

@Database(entities = [LikedSong::class], version = 1)
abstract class LikedSongsRoomDB : RoomDatabase() {
    abstract fun likedSongDao(): LikedSongDao
}



