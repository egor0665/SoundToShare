package com.example.soundtoshare.fragments.map

import android.app.Activity
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.example.soundtoshare.R
import com.example.soundtoshare.external.ObservableUserSongInfo
import com.google.android.gms.maps.GoogleMap.InfoWindowAdapter
import com.google.android.gms.maps.model.Marker


class CustomInfoWindowAdapter(private val context: Activity) : InfoWindowAdapter {
    private var mWindow: View = context.layoutInflater.inflate(R.layout.custom_info_window, null)

    private fun renderWindowText(marker: Marker, view: View) {

        val tvFirstName = view.findViewById<TextView>(R.id.firstName)
        val tvLastName = view.findViewById<TextView>(R.id.lastName)
        val tvSnippet = view.findViewById<TextView>(R.id.snippet)
        val tvAvatar = view.findViewById<ImageView>(R.id.infoWindowAvatar)

        tvFirstName.text = ObservableUserSongInfo.getUserInfo()?.firstName
        tvLastName.text = ObservableUserSongInfo.getUserInfo()?.lastName
        tvSnippet.text = marker.snippet
        tvAvatar.setImageBitmap(ObservableUserSongInfo.getUserInfo()?.avatar)
        view.findViewById<TextView>(R.id.song_name).text = "Kanye East"//ObservableUserSongInfo.songData.value?.title
    }

    override fun getInfoContents(marker: Marker): View {
        renderWindowText(marker, mWindow)
        return mWindow
    }

    override fun getInfoWindow(marker: Marker): View {
        renderWindowText(marker, mWindow)
        return mWindow
    }
}