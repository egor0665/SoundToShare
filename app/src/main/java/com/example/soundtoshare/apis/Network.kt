package com.example.soundtoshare.apis

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import java.net.URL
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class Network {

    fun loadAvatar(iUrl: String, loadAvatarCallback: Bitmap.() -> Unit) {
        val connection = URL(iUrl).openConnection()
        CoroutineScope(Dispatchers.IO).launch {
            loadAvatarCallback(BitmapFactory.decodeStream(connection.inputStream))
        }
    }
}
