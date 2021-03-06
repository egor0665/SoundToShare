package com.example.soundtoshare.fragments.map

import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import android.util.Log
import androidx.lifecycle.LiveData
import com.example.soundtoshare.repositories.CacheRepository
import com.example.soundtoshare.repositories.User
import com.example.soundtoshare.repositories.UserInfoRepository
import com.google.android.gms.location.LocationServices
import com.google.firebase.firestore.GeoPoint

class LocationUpdateUseCase(
    context: Context,
    var userInfoRepository: UserInfoRepository,
    val cacheRepository: CacheRepository
) : LiveData<GeoPoint>() {

    private var fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)

    @SuppressLint("MissingPermission")
    override fun onActive() {
        super.onActive()
        fusedLocationClient.lastLocation
            .addOnSuccessListener { location: Location? ->
                location?.also {
                    setLocationData(it)
                }
            }
    }

    private fun setLocationData(location: Location) {
        value = GeoPoint(
            location.latitude,
            location.longitude
        )
    }

    @SuppressLint("MissingPermission")
    fun uploadData() {
        fusedLocationClient.lastLocation
            .addOnSuccessListener { location: Location? ->
                location?.also {
                    Log.d("Location upload", cacheRepository.getIncognitoMode().toString())

                    Log.d("Location rep", "Location changed $location")
                    Log.d(
                        "Location rep",
                        "Location changed " + location.latitude + location.longitude
                    )
                    if (!cacheRepository.getIncognitoMode() &&
                        cacheRepository.getSongData() != null &&
                        cacheRepository.getSongData()!!.title.isNotEmpty()
                    ) {
                        Log.d("FireStore", "Updated music")
                        val fullName =
                            cacheRepository.getUserInfo().firstName + " " +
                                cacheRepository.getUserInfo().lastName
                        val song = cacheRepository.getSongData()!!.title
                        val artist = cacheRepository.getSongData()!!.artist
                        val id = cacheRepository.getUserInfo().id
                        val avatar = cacheRepository.getUserInfo().avatarUri
                        val user = User(
                            geoPoint = GeoPoint(location.latitude, location.longitude),
                            vkAccount = fullName,
                            song = song,
                            artist = artist,
                            vkAccountID = id,
                            avatar = avatar,
                            lastUpdate = 123
                        )
                        userInfoRepository.storeCurrentUserInfo(user)
                    }
                }
            }
    }
}
