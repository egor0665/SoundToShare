package com.example.soundtoshare.apis

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.widget.ImageView
import kotlinx.coroutines.*
import java.io.IOException
import java.net.URL

object ImageManager {

    fun fetchImage(url: URL, iView: ImageView) {
        val result: Deferred<Bitmap?> = GlobalScope.async {
            url.toBitmap()
        }

        GlobalScope.launch(Dispatchers.Main) {
            iView.setImageBitmap(result.await())
        }
    }

    private fun URL.toBitmap(): Bitmap?{
        return try {
            BitmapFactory.decodeStream(openStream())
        }catch (e: IOException){
            null
        }
    }
}