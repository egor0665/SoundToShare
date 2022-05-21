package com.example.soundtoshare.fragments.map

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.widget.AppCompatButton
import androidx.core.content.ContextCompat
import androidx.lifecycle.MutableLiveData
import com.example.soundtoshare.R
import com.example.soundtoshare.repositories.User
import com.example.soundtoshare.repositories.roomdb.LikedSong
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.GoogleMap.InfoWindowAdapter
import com.google.android.gms.maps.model.Marker


@SuppressLint("ClickableViewAccessibility")
class CustomInfoWindowAdapter(context: Activity, private val infoWindow: ViewGroup,
                              googleMap: GoogleMap?) : InfoWindowAdapter {
    private val mapWrapperLayout: MapWrapperLayout = context.findViewById(R.id.map_relative_layout)
    private var infoButtonListener: OnInfoWindowElemTouchListener
    private var infoButtonListener2: OnInfoWindowElemTouchListener
    private val buttonClicked : MutableLiveData<Pair<User,Int>> by lazy {
        MutableLiveData<Pair<User,Int>>()
    }
    init {
        mapWrapperLayout.init(googleMap, getPixelsFromDp(context, (39 + 20).toFloat()))
        val infoButton1 = infoWindow.findViewById<View>(R.id.buttonLike) as AppCompatButton
        val infoButton2 = infoWindow.findViewById<View>(R.id.buttonPlay) as AppCompatButton
        val icon = infoWindow.findViewById<View>(R.id.avatar) as ImageView

        icon.setImageResource(R.drawable.ic_incognito)


        // Setting custom OnTouchListener which deals with the pressed state
        // so it shows up
        infoButtonListener = object : OnInfoWindowElemTouchListener(
            infoButton1,
            ContextCompat.getDrawable(context, R.drawable.ic_like_thumbs_up_icon),
            ContextCompat.getDrawable(context, R.drawable.ic_like_thumbs_up_icon)
        ) {
            override fun onClickConfirmed(v: View?, marker: Marker?) {
                // Here we can perform some action triggered after clicking the button
                val user = marker!!.tag as User
                buttonClicked.postValue(Pair(user,1))

                Toast.makeText(context, "click on button 1", Toast.LENGTH_SHORT).show()
                buttonClicked.postValue(Pair(user,0))
            }
        }
        infoButton1.setOnTouchListener(infoButtonListener)

        infoButtonListener2 = object : OnInfoWindowElemTouchListener(
            infoButton2,
            ContextCompat.getDrawable(context, R.drawable.ic_circle_fill_play_icon),
            ContextCompat.getDrawable(context, R.drawable.ic_circle_fill_play_icon)
        ) {
            override fun onClickConfirmed(v: View?, marker: Marker?) {
                val user = marker!!.tag as User
                buttonClicked.postValue(Pair(user,2))

                Toast.makeText(context, "click on button 2", Toast.LENGTH_LONG).show()
                buttonClicked.postValue(Pair(user,0))
            }
        }
        infoButton2.setOnTouchListener(infoButtonListener2)
    }

    override fun getInfoContents(marker: Marker): View {

        val infoTitle = infoWindow.findViewById<TextView>(R.id.lastName)
        val infoSnippet = infoWindow.findViewById<TextView>(R.id.firstName)
        // Setting up the infoWindow with current's marker info
        infoSnippet?.text = (marker.tag as User).VKAccount
        infoTitle?.text = (marker.tag as User).artist +" - "+ (marker.tag as User).song
        //ИЛИ (в тэге содержится объект User)
//        val user: User = marker.tag as User
//        infoSnippet?.text = user.VKAccount


        infoButtonListener.setMarker(marker)
        infoButtonListener2.setMarker(marker)
        // We must call this to set the current marker and infoWindow references
        // to the MapWrapperLayout
        mapWrapperLayout.setMarkerWithInfoWindow(marker, infoWindow)
        return infoWindow
    }

    override fun getInfoWindow(marker: Marker): View? {
        return null
    }

    private fun getPixelsFromDp(context: Context, dp: Float): Int {
        val scale = context.resources.displayMetrics.density
        return (dp * scale + 0.5f).toInt()
    }
    fun getObservableButtonClicked(): MutableLiveData<Pair<User,Int>>{
        return buttonClicked
    }


}