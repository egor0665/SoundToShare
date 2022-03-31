package com.example.soundtoshare.databases

import android.util.Log
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.IgnoreExtraProperties
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import java.io.IOException

class FirebaseData {
    private val database = Firebase.database
    private val usersRef = database.getReference("Users")
    private lateinit var spotifyId: String;

    private lateinit var databaseref: DatabaseReference

    fun initializeDbRef() {
        databaseref = Firebase.database.reference
    }

    fun updateUserLocation(altitude: Double, latitude: Double, spotifyAccount: String) {
        val user = User(altitude, latitude)
        spotifyId = spotifyAccount

        // Спотифай аккаунт получать из объекта

        Log.d("Firebase", spotifyId )
        usersRef.child(spotifyId).setValue(user)

    }

    @IgnoreExtraProperties
    data class User(val altitude: Double? = null, val latitude: Double? = null) {
        // Null default values create a no-argument default constructor, which is needed
        // for deserialization from a DataSnapshot.
    }

}