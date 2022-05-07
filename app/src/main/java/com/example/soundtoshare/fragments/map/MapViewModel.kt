package com.example.soundtoshare.fragments.map

import android.R
import android.annotation.SuppressLint
import android.app.Application
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.soundtoshare.repositories.LocationModel
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.android.gms.maps.model.Marker


class MapViewModel(val application: Application, val locationData: GetLocationDataUseCase) : ViewModel(),
    GoogleMap.OnCameraIdleListener,
    GoogleMap.OnCameraMoveListener,
    GoogleMap.OnInfoWindowClickListener {

    private var map: GoogleMap? = null
    private lateinit var moveCameraUseCase: MoveCameraUseCase
    private lateinit var placeUsersUseCase: PlaceUsersUseCase

    val browserIntent: MutableLiveData<Intent> by lazy {
        MutableLiveData<Intent>()
    }

    fun getLocationDataViewModel() = locationData

    private fun getPixelsFromDp(dp: Float): Int {
        val scale: Float = application.resources.displayMetrics.density
        return (dp * scale + 0.5f).toInt()
    }

//    @RequiresApi(Build.VERSION_CODES.M)
//    override fun onMapReady(googleMap: GoogleMap) {
//        map = googleMap
//        map?.setOnCameraIdleListener(this);
//        map?.setOnCameraMoveListener(this)
//        moveCameraUseCase = MoveCameraUseCase(map)
//        placeUsersUseCase = PlaceUsersUseCase(map)
//
//    }

    fun cameraSetUp(lastKnownLocation: LocationModel) {
        moveCameraUseCase.moveAtDeviceCenter(lastKnownLocation)
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
