package com.example.soundtoshare.fragments.map

import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import android.os.Looper
import android.util.Log
import androidx.lifecycle.LiveData
import com.example.soundtoshare.fragments.home.VkGetDataUseCase
import com.example.soundtoshare.fragments.settings.IncognitoModeUseCase
import com.example.soundtoshare.repositories.CacheRepository
import com.example.soundtoshare.repositories.UserInfoRepository
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.firebase.firestore.GeoPoint
import com.vk.api.sdk.VK

class LocationUpdateUseCase(context: Context, var userInfoRepository: UserInfoRepository, cacheRepository: CacheRepository) : LiveData<GeoPoint>()  {

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
            Log.d("Location", cacheRepository.getIncognitoMode().toString())
            for (location in locationResult.locations) {
                Log.d("FireStore", "Updated music")
                if (location != null && !cacheRepository.getIncognitoMode() && cacheRepository.getSongData()!= null
                    && cacheRepository.getSongData()!!.title.isNotEmpty()) {
                        Log.d("FireStore", "Updated music")
                    val fullName = cacheRepository.getUserInfo().firstName + " " + cacheRepository.getUserInfo().lastName
                    val song = cacheRepository.getSongData()!!.title
                    val artist = cacheRepository.getSongData()!!.artist
                    val id = cacheRepository.getUserInfo().id
                    val avatar = cacheRepository.getUserInfo().avatar_uri
                    userInfoRepository.storeCurrentUserInfo(location, fullName, song, artist, id, avatar)
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



