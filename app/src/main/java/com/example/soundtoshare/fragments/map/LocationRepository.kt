package com.example.soundtoshare.fragments.map

import android.location.Location
import android.util.Log
import com.example.soundtoshare.databases.FirestoreDatabase

class LocationRepository {
    var fireStoreDatabase: FirestoreDatabase = FirestoreDatabase()

    fun storeCurrentDeviceLocation(deviceLocation: Location) {
        Log.d("Location", "Location changed $deviceLocation")
        Log.d("Location", "Location changed " + deviceLocation.latitude + deviceLocation.longitude)
        fireStoreDatabase.updateUserInformation(
            deviceLocation.latitude,
            deviceLocation.longitude,
            "kek"
        )
    }
}
