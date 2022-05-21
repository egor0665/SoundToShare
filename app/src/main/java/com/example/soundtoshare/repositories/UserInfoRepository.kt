package com.example.soundtoshare.repositories

import android.location.Location
import android.util.Log
import com.example.soundtoshare.external.FirestoreDatabase

class UserInfoRepository(private val fireStoreDatabase: FirestoreDatabase) {

    fun storeCurrentUserInfo(deviceLocation: Location, vkAccount: String, song: String, artist: String, id : String) {
        Log.d("Location rep", "Location changed $deviceLocation")
        Log.d("Location rep", "Location changed " + deviceLocation.latitude + deviceLocation.longitude)
        fireStoreDatabase.updateUserInformation(
            deviceLocation.latitude,
            deviceLocation.longitude,
            vkAccount,
            song,
            artist,
            id
        )
    }
}