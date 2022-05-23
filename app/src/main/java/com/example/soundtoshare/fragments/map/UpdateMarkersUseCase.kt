package com.example.soundtoshare.fragments.map

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.location.Location
import android.util.Log
import android.view.View
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toBitmap
import com.example.soundtoshare.R
import com.example.soundtoshare.external.FirestoreDatabase
import com.example.soundtoshare.repositories.CacheRepository
import com.example.soundtoshare.repositories.User
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.*
import com.nostra13.universalimageloader.core.DisplayImageOptions
import com.nostra13.universalimageloader.core.ImageLoader
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration
import com.nostra13.universalimageloader.core.assist.FailReason
import com.nostra13.universalimageloader.core.assist.ImageSize
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener
import java.util.*


class UpdateMarkersUseCase(val cacheRepository: CacheRepository, private val fireStoreDatabase: FirestoreDatabase, private val context: Context):
    GoogleMap.OnMarkerClickListener {
    private lateinit var map: GoogleMap
    private var myVkAccount: String = cacheRepository.getUserInfo().id
    private val markersMap = mutableMapOf<String, Marker>()

    fun initUseCase(googleMap: GoogleMap) {
        map = googleMap
    }

    override fun onMarkerClick(marker: Marker): Boolean {
        val options = DisplayImageOptions.Builder().displayer(RoundedBitmapDisplayer(360)).build()
        val imageLoader = ImageLoader.getInstance()
        val user = marker.tag as User
        imageLoader.init(ImageLoaderConfiguration.createDefault(context))
        imageLoader.loadImage(user.avatar, object : ImageLoadingListener {
            override fun onLoadingStarted(imageUri: String?, view: View?) {
            }

            override fun onLoadingFailed(
                imageUri: String?,
                view: View?,
                failReason: FailReason?
            ) {
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
                    marker.showInfoWindow()
                }
            }
            override fun onLoadingCancelled(imageUri: String?, view: View?) {
            }
        })
        return true
    }

    fun getClosest() {
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
                if (user.VKAccountID != myVkAccount && user.VKAccountID != "null"/* && Date().time - user.lastUpdate < 60000*/) {
                    addOrUpdateMarker(user)
                    Log.d("FireStore", "Updated Marker")
                }
            }
        }
    }

    private fun addOrUpdateMarker(newUser: User) {
        val imageLoader = ImageLoader.getInstance()
        imageLoader.init(ImageLoaderConfiguration.createDefault(context))
        imageLoader.loadImage(newUser.avatar, object : ImageLoadingListener {
            override fun onLoadingStarted(imageUri: String?, view: View?) {
            }

            override fun onLoadingFailed(
                imageUri: String?,
                view: View?,
                failReason: FailReason?
            ) {
            }
            override fun onLoadingComplete(
                imageUri: String?,
                view: View?,
                loadedImage: Bitmap?
            ) {
                newUser.bitmap = loadedImage
            }
            override fun onLoadingCancelled(imageUri: String?, view: View?) {
            }
        })




        val oldMarker = markersMap[newUser.VKAccountID]
        if (oldMarker != null) {
            oldMarker.position = LatLng(newUser.geoPoint.latitude, newUser.geoPoint.longitude)
            oldMarker.tag = newUser
        }
        else {
            val userIndicator = MarkerOptions()
                .position(LatLng(newUser.geoPoint.latitude, newUser.geoPoint.longitude))
                .title(newUser.VKAccountID)
                .snippet("lat:" + newUser.geoPoint.latitude + ", lng:" + newUser.geoPoint.longitude)
                .icon(bitmapFromVector(context, R.drawable.ic_circle_dot_record_round_icon))
            val newMarker = map.addMarker(userIndicator)
                //dropPinEffect(newMarker!!)
            newMarker!!.tag = newUser
            markersMap[newUser.VKAccountID] = newMarker
        }
    }

    private fun bitmapFromVector(context: Context, vectorResId: Int): BitmapDescriptor? {
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

    fun removeMarkers() {
        val bounds = map.projection.visibleRegion.latLngBounds
        val invisibleAndOldMarkers = markersMap.filterValues {
            val user = it.tag as User
            it.position.latitude > bounds.northeast.latitude || it.position.longitude > bounds.northeast.longitude ||
            it.position.latitude < bounds.southwest.latitude || it.position.longitude < bounds.southwest.longitude /*||
            Date().time - user.lastUpdate > 60000*/
        }
        invisibleAndOldMarkers.forEach() {
            it.value.remove()
            markersMap.remove(it.key)
        }
    }
}