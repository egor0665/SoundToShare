package com.example.soundtoshare.fragments.map

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import com.google.android.gms.maps.GoogleMap
import com.google.firebase.firestore.GeoPoint


class MapViewModel(val locationUpdate: LocationUpdateUseCase, val updateMarkersUseCase: UpdateMarkersUseCase) : ViewModel(),
    GoogleMap.OnCameraIdleListener {

    var moveCameraUseCase: MoveCameraUseCase? = null

//    val browserIntent: MutableLiveData<Intent> by lazy {
//        MutableLiveData<Intent>()
//    }

    fun startLocationUpdate() = locationUpdate

    fun cameraSetUp(lastKnownLocation: GeoPoint) {
        moveCameraUseCase?.moveAtDeviceCenter(lastKnownLocation)
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCameraIdle() {
        updateMarkersUseCase.removeInvisibleMarkers()
        updateMarkersUseCase.getClosest()
        Log.d("camera changed", "changed")
    }
}
