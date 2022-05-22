package com.example.soundtoshare.fragments.map

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.location.Location
import android.os.Handler
import android.os.Looper
import android.os.SystemClock
import android.util.Log
import android.view.animation.BounceInterpolator
import android.view.animation.Interpolator
import androidx.core.content.ContextCompat
import com.example.soundtoshare.R
import com.example.soundtoshare.external.FirestoreDatabase
import com.example.soundtoshare.repositories.CacheRepository
import com.example.soundtoshare.repositories.User
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.*
import com.google.firebase.Timestamp
import java.util.*
import kotlin.math.max


class UpdateMarkersUseCase(val cacheRepository: CacheRepository, private val fireStoreDatabase: FirestoreDatabase, private val context: Context) {
    private lateinit var map: GoogleMap
    private var myVkAccount: String = cacheRepository.getUserInfo().id
    private val markersMap = mutableMapOf<String, Marker>()

    fun initUseCase(googleMap: GoogleMap) {
        map = googleMap
    }

    fun getClosest() {
//        markersMap.forEach() {
//            val user = it.value.tag as User
//            if (Date().time - user.lastUpdate > 60000) {
//                it.value.remove()
//                markersMap.remove(it.key)
//            }
//        }
        Log.d("time", "kek")
        val results = FloatArray(1)
        Location.distanceBetween(
            map.cameraPosition.target.latitude,
            map.cameraPosition.target.longitude,
            map.projection.visibleRegion.latLngBounds.northeast.latitude,
            map.projection.visibleRegion.latLngBounds.northeast.longitude, results
        )
        fireStoreDatabase.fetchClosest(map.cameraPosition.target, results[0].toDouble()){
            this.forEach() { user ->
                if (user.VKAccountID != myVkAccount)
//                if (user.VKAccountID != myVkAccount && user.VKAccountID != "null" && Date().time - user.lastUpdate < 60000) {
                    addOrUpdateMarker(user)
                    Log.d("FireStore", "Updated Marker")
//                }
            }
        }
    }

    private fun addOrUpdateMarker(newUser: User) {
        val oldMarker = markersMap[newUser.VKAccountID]
        if (oldMarker != null) {
            oldMarker.position = LatLng(newUser.geoPoint.latitude, newUser.geoPoint.longitude)
        }
        else {
            val userIndicator = MarkerOptions()
                .position(LatLng(newUser.geoPoint.latitude, newUser.geoPoint.longitude))
                .title(newUser.VKAccountID)
                .snippet("lat:" + newUser.geoPoint.latitude + ", lng:" + newUser.geoPoint.longitude)
                //.icon(BitmapFromVector(context, R.drawable.ic_circle_dot_record_round_icon))
            val newMarker = map.addMarker(userIndicator)
                //dropPinEffect(newMarker!!)
            newMarker!!.tag = newUser
            markersMap[newUser.VKAccountID] = newMarker
        }
    }

    private fun BitmapFromVector(context: Context, vectorResId: Int): BitmapDescriptor? {
        // below line is use to generate a drawable.
        val vectorDrawable = ContextCompat.getDrawable(context, vectorResId)

        // below line is use to set bounds to our vector drawable.
        vectorDrawable!!.setBounds(
            0,
            0,
            vectorDrawable.intrinsicWidth,
            vectorDrawable.intrinsicHeight
        )

        // below line is use to create a bitmap for our
        // drawable which we have added.
        val bitmap = Bitmap.createBitmap(
            vectorDrawable.intrinsicWidth,
            vectorDrawable.intrinsicHeight,
            Bitmap.Config.ARGB_8888
        )

        // below line is use to add bitmap in our canvas.
        val canvas = Canvas(bitmap)

        // below line is use to draw our
        // vector drawable in canvas.
        vectorDrawable.draw(canvas)

        // after generating our bitmap we are returning our bitmap.
        return BitmapDescriptorFactory.fromBitmap(bitmap)
    }

    private fun dropPinEffect(marker: Marker) {
        val handler = Handler(Looper.getMainLooper())
        val start = SystemClock.uptimeMillis()
        val duration: Long = 1500
        val interpolator: Interpolator = BounceInterpolator()
        handler.post(object : Runnable {
            override fun run() {
                val elapsed = SystemClock.uptimeMillis() - start
                val t = max(
                    1 - interpolator.getInterpolation(
                        elapsed.toFloat()
                                / duration
                    ), 0f
                ).toFloat()
                marker.setAnchor(0.5f, 1.0f + 14 * t)
                if (t > 0.0) {
                    // Post again 15ms later.
                    handler.postDelayed(this, 15)
                }
            }
        })
    }

    fun removeInvisibleMarkers() {
        val bounds = map.projection.visibleRegion.latLngBounds
        val invisibleMarkers = markersMap.filterValues {
            it.position.latitude > bounds.northeast.latitude || it.position.longitude > bounds.northeast.longitude ||
            it.position.latitude < bounds.southwest.latitude || it.position.longitude < bounds.southwest.longitude
        }
        invisibleMarkers.forEach() {
            it.value.remove()
            markersMap.remove(it.key)
        }
    }
}