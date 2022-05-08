package com.example.soundtoshare.fragments.map

import android.location.Location
import android.os.Build
import androidx.annotation.RequiresApi
import com.example.soundtoshare.external.FirestoreDatabase
import com.example.soundtoshare.repositories.User
import com.example.soundtoshare.repositories.VkAPIRepository
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions

class UpdateMarkersUseCase(vkAPIRepository: VkAPIRepository, private val fireStoreDatabase: FirestoreDatabase) {
    private lateinit var map: GoogleMap
    private var myVkAccount: String = vkAPIRepository.getUserInfo()!!.firstName + " " + vkAPIRepository.getUserInfo()!!.lastName
    private val markers = mutableListOf<Marker>()

    fun initUseCase(googleMap: GoogleMap) {
        map = googleMap
        fireStoreDatabase.notify.observeForever(){
            fireStoreDatabase.users.forEach() { user ->
                if (user.VKAccount != myVkAccount && user.VKAccount != "null") {
                    val userIndicator = MarkerOptions()
                        .position(LatLng(user.geoPoint.latitude, user.geoPoint.longitude))
                        .title(user.VKAccount)
                        .snippet("lat:" + user.geoPoint.latitude + ", lng:" + user.geoPoint.longitude)
                    val m = map.addMarker(userIndicator)
                    m?.tag = user
                    processMarker(m!!)
                }
            }
        }
    }

    fun getClosest() {
        val results = FloatArray(1)
        Location.distanceBetween(
            map.cameraPosition.target.latitude,
            map.cameraPosition.target.longitude,
            map.projection.visibleRegion.latLngBounds.northeast.latitude,
            map.projection.visibleRegion.latLngBounds.northeast.longitude, results
        )
        fireStoreDatabase.fetchClosest(map.cameraPosition.target, results[0].toDouble())
    }

    private fun processMarker(marker: Marker) {
        val newUser: User = marker.tag as User
        val oldMarker = markers.find {
            val user: User = it.tag as User
            user.VKAccount == newUser.VKAccount
        }
        if (oldMarker != null) {
            oldMarker.position = marker.position
        }
        else {
            markers.add(marker)
        }
    }

    @RequiresApi(Build.VERSION_CODES.N)
    fun removeInvisibleMarkers() {
        val bounds = map.projection.visibleRegion.latLngBounds
        markers.forEach() {
            if (it.position.latitude > bounds.northeast.latitude || it.position.longitude > bounds.northeast.longitude ||
                it.position.latitude < bounds.southwest.latitude || it.position.longitude < bounds.southwest.longitude) {
                it.remove()
            }
        }
        markers.removeIf {
            it.position.latitude > bounds.northeast.latitude || it.position.longitude > bounds.northeast.longitude ||
            it.position.latitude < bounds.southwest.latitude || it.position.longitude < bounds.southwest.longitude
        }
    }
}