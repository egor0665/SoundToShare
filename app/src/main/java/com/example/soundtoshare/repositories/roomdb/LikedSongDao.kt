package com.example.soundtoshare.repositories.roomdb

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface LikedSongDao {
    @Query("SELECT * FROM LikedSong")
    fun getAll(): MutableList<LikedSong>

    @Query("SELECT * FROM LikedSong WHERE uid IN (:userIds)")
    fun loadAllByIds(userIds: IntArray): List<LikedSong>

    @Query("SELECT * FROM LikedSong WHERE user = (:user) and (:time) - time < 60000")
    fun getLikedWithTime(time: Long, user: String): MutableList<LikedSong>

//    @Query("SELECT * FROM LikedSong WHERE first_name LIKE :first AND " +
//            "last_name LIKE :last LIMIT 1")
//    fun findByName(first: String, last: String): User

    @Insert
    fun insertAll(vararg likedSong: LikedSong)

    @Delete
    fun delete(likedSong: LikedSong)
}
