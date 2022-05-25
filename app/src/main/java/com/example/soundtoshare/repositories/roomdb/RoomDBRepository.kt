package com.example.soundtoshare.repositories.roomdb

import android.content.Context
import androidx.room.Room
import com.example.soundtoshare.repositories.LikedSongsRoomDB
import com.example.soundtoshare.repositories.User
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class RoomDBRepository(context: Context) {
    private val db = Room.databaseBuilder(
        context,
        LikedSongsRoomDB::class.java,
        "LikedSongsRoomDB"
    ).build()
    private val likedSongDao = db.likedSongDao()
    fun getLikedSongs(getLikedSongsCallback: MutableList<LikedSong>.() -> Unit) {
        val ioScope = CoroutineScope(Dispatchers.IO + Job())
        ioScope.launch { getLikedSongsCallback(likedSongDao.getAll()) }
    }

    fun addLikedSong(toUser: User) {
        val ioScope = CoroutineScope(Dispatchers.IO + Job())

        ioScope.launch {
            likedSongDao.insertAll(
                LikedSong(
                    song = toUser.song,
                    artist = toUser.artist,
                    time = System.currentTimeMillis(),
                    user = toUser.VKAccount
                )
            )
        }
    }

    fun checkForLike(toUser: User, getLikedSongsCallback: MutableList<LikedSong>.() -> Unit) {
        val ioScope = CoroutineScope(Dispatchers.IO + Job())
        ioScope.launch {
            getLikedSongsCallback(
                likedSongDao.getLikedWithTime(System.currentTimeMillis(), toUser.VKAccount)
            )
        }
    }
}
