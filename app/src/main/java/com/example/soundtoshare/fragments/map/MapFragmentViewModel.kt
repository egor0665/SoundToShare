package com.example.soundtoshare.fragments.map

import android.annotation.SuppressLint
import android.app.Application
import android.os.Build
import android.util.Log
import android.view.View
import androidx.annotation.RequiresApi
import androidx.lifecycle.AndroidViewModel
import com.example.soundtoshare.R
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.GoogleMap.InfoWindowAdapter
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.android.gms.maps.model.Marker


class MapFragmentViewModel(application: Application) : AndroidViewModel(application), OnMapReadyCallback,
    GoogleMap.OnCameraIdleListener,
    GoogleMap.OnCameraMoveListener {
    private var map: GoogleMap? = null
    var locationPermissionGranted = false
    private val locationData = GetLocationDataUseCase(application)
    private lateinit var moveCameraUseCase: MoveCameraUseCase
    private lateinit var placeUsersUseCase: PlaceUsersUseCase

    fun getLocationData() = locationData

    fun initMap(mapFragment: SupportMapFragment?) {
        mapFragment?.getMapAsync(this)
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap
        map?.setMapStyle(
            MapStyleOptions.loadRawResourceStyle(getApplication<Application>().applicationContext, R.raw.map_without_labels_style)
        )
        map?.setOnCameraIdleListener(this);
        map?.setOnCameraMoveListener(this)
        updateLocationUI()
        moveCameraUseCase = MoveCameraUseCase(map)
        placeUsersUseCase = PlaceUsersUseCase(map)
//        googleMap.setInfoWindowAdapter(object : InfoWindowAdapter {
//            override fun getInfoWindow(marker: Marker): View? {
//                return null
//            }
//
//            override fun getInfoContents(marker: Marker): View? {
//                return null
//            }
//        })
    }

    fun moveCamera(lastKnownLocation: LocationModel) {
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

    override fun onCameraIdle() {
        placeUsersUseCase.placeUsers()
        Log.d("camera changed", "changed")
    }

    override fun onCameraMove() {
        map?.clear()
        //placeUsersUseCase.getClosest(map!!.cameraPosition.target.latitude, map!!.cameraPosition.target.longitude)
    }
}
