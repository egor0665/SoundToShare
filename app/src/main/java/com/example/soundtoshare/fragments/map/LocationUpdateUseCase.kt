package com.example.soundtoshare.fragments.map

import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import android.os.Looper
import android.util.Log
import androidx.lifecycle.LiveData
import com.example.soundtoshare.fragments.home.VkGetDataUseCase
import com.example.soundtoshare.fragments.settings.IncognitoModeUseCase
import com.example.soundtoshare.repositories.UserInfoRepository
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.firebase.firestore.GeoPoint

class LocationUpdateUseCase(context: Context, var userInfoRepository: UserInfoRepository, val incognitoModeUseCase: IncognitoModeUseCase, vkGetDataUseCase: VkGetDataUseCase) : LiveData<GeoPoint>()  {

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
            Log.d("Location", incognitoModeUseCase.getIncognitoMode().toString())
            for (location in locationResult.locations) {

                if (location != null && !incognitoModeUseCase.getIncognitoMode() && vkGetDataUseCase.getSongData()!= null
                    && vkGetDataUseCase.getSongData()!!.title.isNotEmpty()) {

                    val fullName = vkGetDataUseCase.getUserInfo()!!.firstName + " " + vkGetDataUseCase.getUserInfo()!!.lastName
                    val song = vkGetDataUseCase.getSongData()!!.title
                    val artist = vkGetDataUseCase.getSongData()!!.artist
                    userInfoRepository.storeCurrentUserInfo(location, fullName, song, artist)
                    //TODO: Решить, что мы будем сохранять и сделать из этого сущность
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
         value = GeoPoint(location.latitude,
                          location.longitude)
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



