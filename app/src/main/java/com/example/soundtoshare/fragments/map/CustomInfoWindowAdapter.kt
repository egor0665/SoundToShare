package com.example.soundtoshare.fragments.map

import android.app.Activity
import android.content.Context
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.example.soundtoshare.R
import com.example.soundtoshare.external.ObservableUserSongInfo
import com.example.soundtoshare.external.ObservableUserSongInfo.userInfo
import com.google.android.gms.maps.GoogleMap.InfoWindowAdapter
import com.google.android.gms.maps.model.Marker


class CustomInfoWindowAdapter(private val context: Activity) : InfoWindowAdapter {
    private var mWindow: View = context.layoutInflater.inflate(R.layout.custom_info_window, null)

    private fun renderWindowText(marker: Marker, view: View){

        val tvTitle = view.findViewById<TextView>(R.id.title)
        val tvSnippet = view.findViewById<TextView>(R.id.snippet)
        val tvAvatar = view.findViewById<ImageView>(R.id.infoWindowAvatar)

        tvTitle.text = ObservableUserSongInfo.userInfo.value?.lastName
        tvSnippet.text = marker.snippet
        tvAvatar.setImageBitmap(ObservableUserSongInfo.userInfo.value?.avatar)
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