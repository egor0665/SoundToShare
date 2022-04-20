package com.example.soundtoshare.fragments.map

import com.example.soundtoshare.external.FirestoreDatabase
import com.google.android.gms.maps.GoogleMap


class PlaceUsersUseCase(map: GoogleMap?) {
    private val googleMap = map
    private var fireStoreDatabase: FirestoreDatabase = FirestoreDatabase()

    fun placeUsers() {
        fireStoreDatabase.getClosest(googleMap!!)
//        if(fireStoreDatabase.users.isNotEmpty())
//        {
//            fireStoreDatabase.users.forEach() { user ->
//                val userIndicator = MarkerOptions()
//                    .position(LatLng(user.geoPoint.latitude, user.geoPoint.longitude))
//                    .title(user.VKAccount)
//                    .snippet("lat:" + user.geoPoint.latitude + ", lng:" + user.geoPoint.longitude)
//                googleMap.addMarker(userIndicator)
//                Log.d("Placed user" ,user.VKAccount)
//            }
//        }
    }
}