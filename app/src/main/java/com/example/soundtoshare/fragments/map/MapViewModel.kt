package com.example.soundtoshare.fragments.map

import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.soundtoshare.repositories.User
import com.google.android.gms.maps.GoogleMap
import com.google.firebase.firestore.GeoPoint


class MapViewModel(val locationUpdateUseCase: LocationUpdateUseCase, val updateMarkersUseCase: UpdateMarkersUseCase,  val moveCameraUseCase: MoveCameraUseCase, val likePlayUseCase: LikePlayUseCase) : ViewModel(),
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

    fun likeSong(toUser: User) {
        Log.d("reaction", "newreaction")
        likePlayUseCase.likeSong(toUser)
    }

    fun onCamera(){
        updateMarkersUseCase.getClosest()
        updateMarkersUseCase.removeInvisibleMarkers()
    }
}