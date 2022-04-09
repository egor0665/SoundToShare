package com.example.soundtoshare

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Handler
import android.os.Message
import android.util.Log
import android.widget.ImageView
import java.io.BufferedInputStream
import java.io.IOException
import java.net.HttpURLConnection
import java.net.MalformedURLException
import java.net.URL


object ImageManager {
    private const val TAG = "ImageManager"
    fun fetchImage(iUrl: String?, iView: ImageView?) {
        if (iUrl == null || iView == null) return
        val handler: Handler = object : Handler() {
            override fun handleMessage(message: Message) {
                val image = message.obj as Bitmap
                iView.setImageBitmap(image)
            }
        }
        val thread: Thread = object : Thread() {
            override fun run() {
                val image = downloadImage(iUrl)
                if (image != null) {
                    Log.v(TAG, "Got image by URL: $iUrl")
                    val message = handler.obtainMessage(1, image)
                    handler.sendMessage(message)
                }
            }
        }
        thread.priority = 3
        thread.start()
    }

    fun downloadImage(iUrl: String): Bitmap? {
        var bitmap: Bitmap? = null
        var conn: HttpURLConnection? = null
        var buf_stream: BufferedInputStream? = null
        try {
            Log.v(TAG, "Starting loading image by URL: $iUrl")
            conn = URL(iUrl).openConnection() as HttpURLConnection
            conn.doInput = true
            conn!!.setRequestProperty("Connection", "Keep-Alive")
            conn.connect()
            buf_stream = BufferedInputStream(conn.inputStream, 8192)
            bitmap = BitmapFactory.decodeStream(buf_stream)
            buf_stream.close()
            conn.disconnect()
            buf_stream = null
            conn = null
        } catch (ex: MalformedURLException) {
            Log.e(TAG, "Url parsing was failed: $iUrl")
        } catch (ex: IOException) {
            Log.d(TAG, "$iUrl does not exists")
        } catch (e: OutOfMemoryError) {
            Log.w(TAG, "Out of memory!!!")
            return null
        } finally {
            if (buf_stream != null) try {
                buf_stream.close()
            } catch (ex: IOException) {
            }
            conn?.disconnect()
        }
        return bitmap
    }
}