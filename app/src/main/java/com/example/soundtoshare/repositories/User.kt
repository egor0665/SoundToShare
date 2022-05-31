package com.example.soundtoshare.repositories

import android.graphics.Bitmap
import com.google.firebase.firestore.GeoPoint

data class User(
    val geoPoint: GeoPoint,
    val vkAccount: String,
    val lastUpdate: Long,
    val vkAccountID: String,
    val song: String = "",
    val artist: String = "",
    val avatar: String = "",
    var bitmap: Bitmap? = null
)
