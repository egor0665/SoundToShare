package com.example.soundtoshare.repositories

import com.google.firebase.Timestamp
import com.google.firebase.firestore.GeoPoint


data class User(val geoPoint: GeoPoint, val VKAccount: String, val lastUpdate: Long, val VKAccountID : String, val song:String="", val artist: String = "")