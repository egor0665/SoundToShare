package com.example.soundtoshare.fragments.map

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.example.soundtoshare.R
import com.example.soundtoshare.repositories.User
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.GoogleMap.InfoWindowAdapter
import com.google.android.gms.maps.model.Marker


@SuppressLint("ClickableViewAccessibility")
class CustomInfoWindowAdapter(private val context: Activity, private val infoWindow: ViewGroup,
                              googleMap: GoogleMap?) : InfoWindowAdapter {
    private val mapWrapperLayout: MapWrapperLayout = context.findViewById(R.id.map_relative_layout)
    private var infoButtonListener: OnInfoWindowElemTouchListener
    private var infoButtonListener2: OnInfoWindowElemTouchListener

    init {
        mapWrapperLayout.init(googleMap, getPixelsFromDp(context, (39 + 20).toFloat()))
        val infoButton1 = infoWindow.findViewById<View>(R.id.btnOne) as Button
        val infoButton2 = infoWindow.findViewById<View>(R.id.btnTwo) as Button

        // Setting custom OnTouchListener which deals with the pressed state
        // so it shows up
        infoButtonListener = object : OnInfoWindowElemTouchListener(
            infoButton1,
            ContextCompat.getDrawable(context, android.R.drawable.btn_default),
            ContextCompat.getDrawable(context, android.R.drawable.btn_default_small)
        ) {
            override fun onClickConfirmed(v: View?, marker: Marker?) {
                // Here we can perform some action triggered after clicking the button
                //TODO: Реализовать кнопки
                Toast.makeText(context, "click on button 1", Toast.LENGTH_SHORT).show()
            }
        }
        infoButton1.setOnTouchListener(infoButtonListener)

        infoButtonListener2 = object : OnInfoWindowElemTouchListener(
            infoButton2,
            ContextCompat.getDrawable(context, android.R.drawable.btn_default),
            ContextCompat.getDrawable(context, android.R.drawable.btn_default_small)
        ) {
            override fun onClickConfirmed(v: View?, marker: Marker?) {
                //TODO: Реализовать кнопки
                Toast.makeText(context, "click on button 2", Toast.LENGTH_LONG).show()
            }
        }
        infoButton2.setOnTouchListener(infoButtonListener2)
    }

    override fun getInfoContents(marker: Marker): View {
        val infoTitle = infoWindow.findViewById<TextView>(R.id.title)
        val infoSnippet = infoWindow.findViewById<TextView>(R.id.snippet)
        // Setting up the infoWindow with current's marker info
        infoSnippet?.text = marker.title
        infoTitle?.text = marker.snippet
        //ИЛИ (в тэге содержится объект User)
        val user: User = marker.tag as User
        infoSnippet?.text = user.VKAccount


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


}