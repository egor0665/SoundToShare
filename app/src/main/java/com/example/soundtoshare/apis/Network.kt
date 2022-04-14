package com.example.soundtoshare.apis

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import kotlinx.coroutines.*
import java.net.URL

class Network {

    fun loadAvatar(iUrl: String, loadAvatarCallback: Bitmap.() -> Unit) {
        val connection = URL(iUrl).openConnection()
        CoroutineScope(Dispatchers.IO).launch {
            loadAvatarCallback(BitmapFactory.decodeStream(connection.inputStream))
        }
    }
}