package com.example.soundtoshare.external

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.SupervisorJob

import java.net.URL

class Network {

    private val scope = CoroutineScope(Dispatchers.Default + SupervisorJob())

    private var loadJob: Job? = null

    fun loadAvatar(iUrl: String, callback: Bitmap.() -> Unit) {
        val connection = URL(iUrl).openConnection()

        loadJob?.cancel()
        loadJob = null

        loadJob = scope.launch {
            callback(BitmapFactory.decodeStream(connection.inputStream))
        }
    }
}
