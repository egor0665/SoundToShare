package com.example.soundtoshare.fragments.map

import android.util.Log
import androidx.lifecycle.ViewModel
import com.google.android.gms.maps.GoogleMap
import com.google.firebase.firestore.GeoPoint


class MapViewModel(val locationUpdateUseCase: LocationUpdateUseCase, val updateMarkersUseCase: UpdateMarkersUseCase,  var moveCameraUseCase: MoveCameraUseCase,  likePlayUseCase: LikePlayUseCase) : ViewModel(),
    GoogleMap.OnCameraIdleListener {

//   val user: MutableLiveData<User> by lazy {
//       MutableLiveData<User>()
//    }

    fun startLocationUpdate() = locationUpdateUseCase

    fun cameraSetUp(lastKnownLocation: GeoPoint) {
        moveCameraUseCase.moveAtDeviceCenter(lastKnownLocation)
    }

    override fun onCameraIdle() {
        updateMarkersUseCase.getClosest()
        updateMarkersUseCase.removeInvisibleMarkers()
        Log.d("camera changed", "changed")
    }

    fun likeSong(from: String, fromId: Int,song: String, artist:String, avatar: String) {
//        likePlayUseCase.likeSong(from, fromId,song, artist, avatar)
    }

    fun onCamera(){
        updateMarkersUseCase.getClosest()
        updateMarkersUseCase.removeInvisibleMarkers()
    }
}