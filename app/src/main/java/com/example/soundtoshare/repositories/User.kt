package com.example.soundtoshare.repositories

import com.google.firebase.firestore.GeoPoint

data class User(val geoPoint: GeoPoint, val VKAccount: String)