package com.example.soundtoshare.fragments.map

import com.example.soundtoshare.repositories.LocationModel
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng

class MoveCameraUseCase(map: GoogleMap?) {
    private val googleMap = map

    companion object {
        private const val DEFAULT_ZOOM = 15
    }

    fun moveAtDeviceCenter(deviceLocation: LocationModel) {
        googleMap?.moveCamera(
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
