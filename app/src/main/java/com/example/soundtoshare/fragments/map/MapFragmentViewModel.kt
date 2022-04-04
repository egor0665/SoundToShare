package com.example.soundtoshare.fragments.map

import android.annotation.SuppressLint
import android.app.Application
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.AndroidViewModel
import com.example.soundtoshare.R
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.MapStyleOptions


class MapFragmentViewModel(application: Application) : AndroidViewModel(application), OnMapReadyCallback{
    @SuppressLint("StaticFieldLeak")
    private var context = getApplication<Application>().applicationContext
    private var map: GoogleMap? = null
    var locationPermissionGranted = false
    private val locationData = GetLocationDataUseCase(application)
    private lateinit var moveCameraUseCase: MoveCameraUseCase

    fun getLocationData() = locationData

    fun initMap(mapFragment: SupportMapFragment?)
    {
        mapFragment?.getMapAsync(this)
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap
        map?.setMapStyle(
            MapStyleOptions.loadRawResourceStyle(context, R.raw.map_without_labels_style)
        )
        updateLocationUI()
        moveCameraUseCase = MoveCameraUseCase(map)
    }

    fun moveCamera(lastKnownLocation: LocationModel)
    {
        moveCameraUseCase.moveAtDeviceCenter(lastKnownLocation)
    }

    @SuppressLint("MissingPermission")
    fun updateLocationUI() {
        if (locationPermissionGranted) {
            map?.isMyLocationEnabled = true
            map?.uiSettings?.isMyLocationButtonEnabled = true
        } else {
            map?.isMyLocationEnabled = false
            map?.uiSettings?.isMyLocationButtonEnabled = false
        }
    }

}
