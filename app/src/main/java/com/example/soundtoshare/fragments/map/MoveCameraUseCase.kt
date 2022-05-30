package com.example.soundtoshare.fragments.map

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng
import com.google.firebase.firestore.GeoPoint

class MoveCameraUseCase {
    private lateinit var map: GoogleMap

    fun initUseCase(googleMap: GoogleMap) {
        map = googleMap
    }
    companion object {
        private const val DEFAULT_ZOOM = 15
    }

    fun moveAtDeviceCenter(deviceLocation: GeoPoint) {
        map.moveCamera(
            CameraUpdateFactory.newLatLngZoom(
                LatLng(
                    deviceLocation.latitude,
                    deviceLocation.longitude
                ),
                DEFAULT_ZOOM.toFloat()
            )
        )
    }
}
