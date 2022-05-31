package com.example.soundtoshare.repositories.roomdb

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class LikedSong(
    @PrimaryKey(autoGenerate = true) val uid: Int = 0,
    @ColumnInfo(name = "artist") val artist: String?,
    @ColumnInfo(name = "song") val song: String?,
    @ColumnInfo(name = "time") val time: Long?,
    @ColumnInfo(name = "user") val user: String?,
)
