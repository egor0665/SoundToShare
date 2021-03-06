package com.example.soundtoshare.fragments.map

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.location.Location
import android.net.Uri
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.example.soundtoshare.R
import com.example.soundtoshare.external.FirestoreDatabase
import com.example.soundtoshare.repositories.CacheRepository
import com.example.soundtoshare.repositories.User
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.nostra13.universalimageloader.core.ImageLoader
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration
import com.nostra13.universalimageloader.core.assist.FailReason
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener
import java.util.Date

class UpdateMarkersUseCase(
    val cacheRepository: CacheRepository,
    private val fireStoreDatabase: FirestoreDatabase,
    private val context: Context
) :
    GoogleMap.OnMarkerClickListener,
    GoogleMap.OnInfoWindowClickListener {
    private lateinit var map: GoogleMap
    private var myVkAccount: String = cacheRepository.getUserInfo().id
    private val markersMap = mutableMapOf<String, Marker>()
    private val imageLoader = ImageLoader.getInstance()

    fun initUseCase(googleMap: GoogleMap) {
        map = googleMap
        imageLoader.init(ImageLoaderConfiguration.createDefault(context))
    }

    override fun onInfoWindowClick(marker: Marker) {
        val user = marker.tag as User
        context.startActivity(
            Intent(Intent.ACTION_VIEW, Uri.parse("https://vk.com/id" + user.vkAccountID)).setFlags(
                Intent.FLAG_ACTIVITY_NEW_TASK
            )
        )
    }

    override fun onMarkerClick(marker: Marker): Boolean {
        val user = marker.tag as User
        if (user.bitmap == null) {

            imageLoader.loadImage(
                user.avatar,
                object : ImageLoadingListener {
                    override fun onLoadingStarted(imageUri: String?, view: View?) {
                        Log.d("Image loading", "Started")
                    }

                    override fun onLoadingFailed(
                        imageUri: String?,
                        view: View?,
                        failReason: FailReason?
                    ) {
                        Toast.makeText(
                            context,
                            "Failed to load avatar",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                    override fun onLoadingComplete(
                        imageUri: String?,
                        view: View?,
                        loadedImage: Bitmap?
                    ) {
                        Log.d("kek", "lel")
                        user.bitmap = loadedImage
                        marker.tag = user
                        if (marker.isInfoWindowShown) {
                            marker.hideInfoWindow()
                        } else {
                            map.animateCamera(
                                CameraUpdateFactory.newLatLngZoom(
                                    LatLng(
                                        user.geoPoint.latitude,
                                        user.geoPoint.longitude
                                    ),
                                    map.cameraPosition.zoom
                                )
                            )
                            marker.showInfoWindow()
                        }
                    }
                    override fun onLoadingCancelled(imageUri: String?, view: View?) {
                        Log.d("Image loading", "Cancelled")
                    }
                }
            )
        } else {
            if (marker.isInfoWindowShown) {
                marker.hideInfoWindow()
            } else {
                map.animateCamera(
                    CameraUpdateFactory.newLatLngZoom(
                        LatLng(user.geoPoint.latitude, user.geoPoint.longitude),
                        map.cameraPosition.zoom
                    )
                )
                marker.showInfoWindow()
            }
        }
        return true
    }

    fun getClosest() {
        Log.d("time", "kek")
        val results = FloatArray(1)
        Location.distanceBetween(
            map.cameraPosition.target.latitude,
            map.cameraPosition.target.longitude,
            map.projection.visibleRegion.latLngBounds.northeast.latitude,
            map.projection.visibleRegion.latLngBounds.northeast.longitude,
            results
        )
        fireStoreDatabase.fetchClosest(map.cameraPosition.target, results[0].toDouble()) {
            this.forEach() { user ->
                if (user.vkAccountID != myVkAccount && user.vkAccountID != "null" && Date().time -
                    user.lastUpdate < oneMinuteDelay
                ) {
                    addOrUpdateMarker(user)
                    Log.d("FireStore", "Updated Marker")
                }
            }
        }
    }

    private fun addOrUpdateMarker(newUser: User) {

        val oldMarker = markersMap[newUser.vkAccountID]
        if (oldMarker != null) {
            oldMarker.position = LatLng(newUser.geoPoint.latitude, newUser.geoPoint.longitude)
            newUser.bitmap = (oldMarker.tag as User).bitmap
            oldMarker.tag = newUser
        } else {
            val userIndicator = MarkerOptions()
                .position(LatLng(newUser.geoPoint.latitude, newUser.geoPoint.longitude))
                .title(newUser.vkAccountID)
                .snippet("lat:" + newUser.geoPoint.latitude + ", lng:" + newUser.geoPoint.longitude)
                .icon(bitmapFromVector(context, R.drawable.ic_circle_dot_record_round_icon))
                .anchor(markerAnchor, markerAnchor)
            val newMarker = map.addMarker(userIndicator)
            newMarker!!.tag = newUser
            markersMap[newUser.vkAccountID] = newMarker
        }
    }

    private fun bitmapFromVector(context: Context, vectorResId: Int): BitmapDescriptor {
        val vectorDrawable = ContextCompat.getDrawable(context, vectorResId)

        vectorDrawable!!.setBounds(
            0,
            0,
            vectorDrawable.intrinsicWidth,
            vectorDrawable.intrinsicHeight
        )

        val bitmap = Bitmap.createBitmap(
            vectorDrawable.intrinsicWidth,
            vectorDrawable.intrinsicHeight,
            Bitmap.Config.ARGB_8888
        )

        val canvas = Canvas(bitmap)
        vectorDrawable.draw(canvas)

        return BitmapDescriptorFactory.fromBitmap(bitmap)
    }

    fun removeMarkers() {
        val bounds = map.projection.visibleRegion.latLngBounds
        val invisibleAndOldMarkers = markersMap.filterValues {
            val user = it.tag as User
            it.position.latitude > bounds.northeast.latitude || it.position.longitude >
                bounds.northeast.longitude ||
                it.position.latitude < bounds.southwest.latitude || it.position.longitude <
                bounds.southwest.longitude ||
                Date().time - user.lastUpdate > oneMinuteDelay
        }
        invisibleAndOldMarkers.forEach() {
            it.value.remove()
            markersMap.remove(it.key)
        }
    }

    companion object {
        const val oneMinuteDelay = 60000
        const val markerAnchor = 0.5f
    }
}
