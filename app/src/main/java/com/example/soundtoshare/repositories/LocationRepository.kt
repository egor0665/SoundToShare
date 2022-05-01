package com.example.soundtoshare.repositories

import android.location.Location
import android.util.Log
import com.example.soundtoshare.external.FirestoreDatabase

class LocationRepository {
    var fireStoreDatabase: FirestoreDatabase = FirestoreDatabase()

    fun storeCurrentDeviceLocation(deviceLocation: Location, vkAccount: String, song: String, artist: String) {
        Log.d("Location", "Location changed $deviceLocation")
        Log.d("Location", "Location changed " + deviceLocation.latitude + deviceLocation.longitude)
        fireStoreDatabase.updateUserInformation(
            deviceLocation.latitude,
            deviceLocation.longitude,
            vkAccount,
            song,
            artist
        )
    }
}