package com.example.soundtoshare.fragments.map

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.graphics.*
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.widget.AppCompatButton
import androidx.core.content.ContextCompat
import androidx.lifecycle.MutableLiveData
import com.example.soundtoshare.R
import com.example.soundtoshare.repositories.User
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.GoogleMap.InfoWindowAdapter
import com.google.android.gms.maps.model.Marker

@SuppressLint("ClickableViewAccessibility")
class CustomInfoWindowAdapter(
    context: Activity,
    private val infoWindow: ViewGroup,
    googleMap: GoogleMap?
) : InfoWindowAdapter {
    private val mapWrapperLayout: MapWrapperLayout = context.findViewById(R.id.map_relative_layout)
    private var infoButtonListener: OnInfoWindowElemTouchListener
    private var infoButtonListener2: OnInfoWindowElemTouchListener
    private val buttonClicked: MutableLiveData<Pair<User, Int>> by lazy {
        MutableLiveData<Pair<User, Int>>()
    }
    init {
        mapWrapperLayout.init(googleMap, getPixelsFromDp(context, 15.toFloat()))
        val infoButton1 = infoWindow.findViewById<View>(R.id.buttonLike) as AppCompatButton
        val infoButton2 = infoWindow.findViewById<View>(R.id.buttonPlay) as AppCompatButton

        // Setting custom OnTouchListener which deals with the pressed state
        // so it shows up
        infoButtonListener = object : OnInfoWindowElemTouchListener(
            infoButton1,
            ContextCompat.getDrawable(context, R.drawable.ic_like_thumbs_up_icon),
            ContextCompat.getDrawable(context, R.drawable.ic_like_thumbs_up_icon_map_reversed)
        ) {
            override fun onClickConfirmed(v: View?, marker: Marker?) {
                val user = marker!!.tag as User
                buttonClicked.postValue(Pair(user, 1))
            }
        }
        infoButton1.setOnTouchListener(infoButtonListener)

        infoButtonListener2 = object : OnInfoWindowElemTouchListener(
            infoButton2,
            ContextCompat.getDrawable(context, R.drawable.ic_circle_fill_play_icon),
            ContextCompat.getDrawable(context, R.drawable.ic_circle_fill_play_icon_reversed)
        ) {
            override fun onClickConfirmed(v: View?, marker: Marker?) {
                val user = marker!!.tag as User
                buttonClicked.postValue(Pair(user, 2))
            }
        }
        infoButton2.setOnTouchListener(infoButtonListener2)
    }

    override fun getInfoContents(marker: Marker): View {
        return infoWindow
    }

    override fun getInfoWindow(marker: Marker): View {
        val infoTitle = infoWindow.findViewById<TextView>(R.id.lastName)
        val infoSnippet = infoWindow.findViewById<TextView>(R.id.firstName)
        val icon = infoWindow.findViewById<View>(R.id.avatar) as ImageView
        // Setting up the infoWindow with current's marker info
        val user: User = marker.tag as User
        infoSnippet?.text = user.VKAccount
        infoTitle?.text = user.artist + " - " + user.song

        if (user.bitmap != null)
            icon.setImageBitmap(getRoundedCornerBitmap(user.bitmap!!, 40))

        infoButtonListener.setMarker(marker)
        infoButtonListener2.setMarker(marker)
        // We must call this to set the current marker and infoWindow references
        // to the MapWrapperLayout
        mapWrapperLayout.setMarkerWithInfoWindow(marker, infoWindow)
        return infoWindow
    }

    private fun getRoundedCornerBitmap(bitmap: Bitmap, pixels: Int): Bitmap? {
        val output = Bitmap.createBitmap(bitmap.width, bitmap.height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(output)
        val paint = Paint()
        val rect = Rect(0, 0, bitmap.width, bitmap.height)
        val rectF = RectF(Rect(0, 0, bitmap.width, bitmap.height))
        paint.isAntiAlias = true
        canvas.drawARGB(0, 0, 0, 0)
        paint.color = -0xbdbdbe
        canvas.drawRoundRect(rectF, bitmap.height.toFloat(), bitmap.height.toFloat(), paint)
        paint.xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_IN)
        canvas.drawBitmap(bitmap, rect, rect, paint)
        return output
    }

    private fun getPixelsFromDp(context: Context, dp: Float): Int {
        val scale = context.resources.displayMetrics.density
        return (dp * scale + 0.5f).toInt()
    }
    fun getObservableButtonClicked(): MutableLiveData<Pair<User, Int>> {
        return buttonClicked
    }
}
