package com.example.soundtoshare.apis

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import kotlinx.coroutines.*
import java.io.IOException
import java.net.URL

class Network {

    fun loadAvatar(iUrl: String, loadAvatarCallback: Bitmap.() -> Unit) {
        val conn = URL(iUrl).openConnection()
        CoroutineScope(Dispatchers.IO).launch {
            withContext(Dispatchers.IO) {
                try {
                    conn.doInput = true
                    val bufStream = conn.inputStream
                    loadAvatarCallback(BitmapFactory.decodeStream(bufStream))
                } catch (ex: IOException) {
                }
            }
        }
    }
}