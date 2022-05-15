package com.example.soundtoshare.fragments.map

import android.annotation.SuppressLint
import android.app.Application
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.soundtoshare.R
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.android.gms.maps.model.Marker


class MapViewModel(val application: Application, val locationUpdate: LocationUpdateUseCase) : ViewModel(), OnMapReadyCallback,
    GoogleMap.OnCameraIdleListener,
    GoogleMap.OnCameraMoveListener,
    GoogleMap.OnInfoWindowClickListener {

    private var map: GoogleMap? = null
    var locationPermissionGranted = false
    private lateinit var customInfoWindowAdapter: CustomInfoWindowAdapter
    private lateinit var moveCameraUseCase: MoveCameraUseCase
    private lateinit var placeUsersUseCase: PlaceUsersUseCase

    val browserIntent: MutableLiveData<Intent> by lazy {
        MutableLiveData<Intent>()
    }

    fun getLocationDataViewModel() = locationUpdate

    fun initMap(mapFragment: SupportMapFragment?, customWindowAdapter: CustomInfoWindowAdapter) {
        mapFragment?.getMapAsync(this)
        customInfoWindowAdapter = customWindowAdapter
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap
        map?.setMapStyle(
            MapStyleOptions.loadRawResourceStyle(application, R.raw.map_without_labels_style)
        )
        map?.setOnCameraIdleListener(this);
        map?.setOnCameraMoveListener(this)
        updateLocationUI()
        moveCameraUseCase = MoveCameraUseCase(map)
        placeUsersUseCase = PlaceUsersUseCase(map)
        map?.setInfoWindowAdapter(customInfoWindowAdapter)
        map?.setOnInfoWindowClickListener(this)
    }

    fun cameraSetUp(lastKnownLocation: LocationModel) {
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

    override fun onInfoWindowClick(marker: Marker) {
        Log.d("InfoWindowClick", "Clicked")
        browserIntent.value = Intent(Intent.ACTION_VIEW, Uri.parse("http://www.google.com"))
    }
}
