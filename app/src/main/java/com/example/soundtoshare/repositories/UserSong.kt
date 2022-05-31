package com.example.soundtoshare.repositories

import com.google.firebase.Timestamp
import com.google.firebase.firestore.GeoPoint

data class UserSong(
    val geoPoint: GeoPoint,
    val vkAccount: String,
    val lastUpdate: Timestamp,
    val song: String,
    val artist: String
)
