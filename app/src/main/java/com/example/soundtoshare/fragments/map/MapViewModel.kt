package com.example.soundtoshare.fragments.map

import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.soundtoshare.repositories.User
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.Marker
import com.google.firebase.firestore.GeoPoint


class MapViewModel(val locationUpdateUseCase: LocationUpdateUseCase, val updateMarkersUseCase: UpdateMarkersUseCase,  val moveCameraUseCase: MoveCameraUseCase, val likePlayUseCase: LikePlayUseCase) : ViewModel(),
    GoogleMap.OnCameraIdleListener{

    fun startLocationUpdate() = locationUpdateUseCase

    fun cameraSetUp(lastKnownLocation: GeoPoint) {
        moveCameraUseCase.moveAtDeviceCenter(lastKnownLocation)
    }

    override fun onCameraIdle() {
        updateMarkersUseCase.removeMarkers()
        updateMarkersUseCase.getClosest()
        Log.d("Load users", "changed")
    }

    fun likeSong(toUser: User) {
        Log.d("reaction", "newreaction")
        likePlayUseCase.likeSong(toUser)
    }
}