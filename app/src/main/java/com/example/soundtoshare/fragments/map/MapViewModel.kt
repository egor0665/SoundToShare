package com.example.soundtoshare.fragments.map

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import com.google.android.gms.maps.GoogleMap
import com.google.firebase.firestore.GeoPoint


class MapViewModel(val locationUpdate: LocationUpdateUseCase, val updateMarkersUseCase: UpdateMarkersUseCase, val likePlayUseCase: LikePlayUseCase) : ViewModel(),
    GoogleMap.OnCameraIdleListener {
    var moveCameraUseCase: MoveCameraUseCase? = null

    fun startLocationUpdate() = locationUpdate

    fun cameraSetUp(lastKnownLocation: GeoPoint) {
        moveCameraUseCase?.moveAtDeviceCenter(lastKnownLocation)
    }

    override fun onCameraIdle() {
        updateMarkersUseCase.removeInvisibleMarkers()
        updateMarkersUseCase.getClosest()
        Log.d("camera changed", "changed")
    }

    fun likeSong(from: String, fromId: Int,song: String, artist:String, avatar: String) {
        likePlayUseCase.likeSong(from, fromId,song, artist, avatar)
    }

}