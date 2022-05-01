package com.example.soundtoshare.fragments.map

import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import android.os.Looper
import android.util.Log
import androidx.lifecycle.LiveData
import com.example.soundtoshare.repositories.LocationRepository
import com.example.soundtoshare.repositories.SharedPreferencesRepository
import com.example.soundtoshare.repositories.VkAPIRepository
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices

class GetLocationDataUseCase(context: Context, var locationRepository: LocationRepository, val sharedPreferencesRepository: SharedPreferencesRepository, vkAPIRepository: VkAPIRepository) : LiveData<LocationModel>()  {
    private var fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)

    companion object {
        val locationRequest: LocationRequest = LocationRequest.create().apply {
            interval = 10000
            fastestInterval = 5000
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
            maxWaitTime = 10000
        }
    }

    private val locationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            Log.d("Location", "Location changed")
            for (location in locationResult.locations) {

                if (location != null && !sharedPreferencesRepository.getIncognitoMode() && vkAPIRepository.getSongData()!= null && vkAPIRepository.getSongData()!!.title.isNotEmpty()) {
                    val fullName = vkAPIRepository.getUserInfo()!!.firstName + " " + vkAPIRepository.getUserInfo()!!.lastName
                    val song = vkAPIRepository.getSongData()!!.title
                    val artist = vkAPIRepository.getSongData()!!.artist
                    locationRepository.storeCurrentDeviceLocation(location, fullName, song, artist)
                }
            }
        }
    }

    override fun onInactive() {
        super.onInactive()
        fusedLocationClient.removeLocationUpdates(locationCallback)
    }

    @SuppressLint("MissingPermission")
    override fun onActive() {
        super.onActive()
        fusedLocationClient.lastLocation
            .addOnSuccessListener { location: Location? ->
                location?.also {
                    setLocationData(it)
                }
            }
        startLocationUpdates()
    }

    private fun setLocationData(location: Location) {
        value = LocationModel(
            longitude = location.longitude,
            latitude = location.latitude
        )
    }

    @SuppressLint("MissingPermission")
    private fun startLocationUpdates() {
        fusedLocationClient.requestLocationUpdates(
            locationRequest,
            locationCallback,
            Looper.getMainLooper()
        )
    }
}

data class LocationModel(val longitude: Double, val latitude: Double)

