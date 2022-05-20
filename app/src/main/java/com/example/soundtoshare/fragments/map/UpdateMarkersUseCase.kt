package com.example.soundtoshare.fragments.map

import android.location.Location
import android.os.Handler
import android.os.Looper
import android.os.SystemClock
import android.view.animation.BounceInterpolator
import android.view.animation.Interpolator
import com.example.soundtoshare.external.FirestoreDatabase
import com.example.soundtoshare.fragments.home.VkGetDataUseCase
import com.example.soundtoshare.repositories.User
import com.example.soundtoshare.repositories.VkAPIRepository
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.firebase.Timestamp
import kotlin.math.max


class UpdateMarkersUseCase(vkGetDataUseCase: VkGetDataUseCase, private val fireStoreDatabase: FirestoreDatabase) {
    private lateinit var map: GoogleMap
    private lateinit var myVkAccount: String
    private val markersMap = mutableMapOf<String, Marker>()

    init {
        vkGetDataUseCase.loadUserInfo{
            myVkAccount = this.firstName + " " +  this.lastName
        }
    }

    fun initUseCase(googleMap: GoogleMap) {
        map = googleMap
        fireStoreDatabase.notify.observeForever(){
            fireStoreDatabase.users.forEach() { user ->
                if (user.VKAccount != myVkAccount && user.VKAccount != "null" && user.lastUpdate.toDate().time - Timestamp.now().toDate().time < 60000) {
                    addOrUpdateMarker(user)
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

    private fun addOrUpdateMarker(newUser: User) {
        val oldMarker = markersMap[newUser.VKAccount]
        if (oldMarker != null) {
            oldMarker.position = LatLng(newUser.geoPoint.latitude, newUser.geoPoint.longitude)
        }
        else {
            val userIndicator = MarkerOptions()
                .position(LatLng(newUser.geoPoint.latitude, newUser.geoPoint.longitude))
                .title(newUser.VKAccount)
                .snippet("lat:" + newUser.geoPoint.latitude + ", lng:" + newUser.geoPoint.longitude)
            val newMarker = map.addMarker(userIndicator)
            dropPinEffect(newMarker!!)
            newMarker.tag = newUser
            markersMap[newUser.VKAccount] = newMarker
        }
    }

    private fun dropPinEffect(marker: Marker) {
        val handler = Handler(Looper.getMainLooper())
        val start = SystemClock.uptimeMillis()
        val duration: Long = 1500
        val interpolator: Interpolator = BounceInterpolator()
        handler.post(object : Runnable {
            override fun run() {
                val elapsed = SystemClock.uptimeMillis() - start
                val t = max(
                    1 - interpolator.getInterpolation(
                        elapsed.toFloat()
                                / duration
                    ), 0f
                ).toFloat()
                marker.setAnchor(0.5f, 1.0f + 14 * t)
                if (t > 0.0) {
                    // Post again 15ms later.
                    handler.postDelayed(this, 15)
                }
            }
        })
    }

    fun removeInvisibleMarkers() {
        val bounds = map.projection.visibleRegion.latLngBounds
        val invisibleMarkers = markersMap.filterValues {
            it.position.latitude > bounds.northeast.latitude || it.position.longitude > bounds.northeast.longitude ||
            it.position.latitude < bounds.southwest.latitude || it.position.longitude < bounds.southwest.longitude
        }
        invisibleMarkers.forEach() {
            it.value.remove()
            markersMap.remove(it.key)
        }
    }
}