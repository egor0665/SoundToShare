package com.example.soundtoshare.fragments.map

import android.graphics.drawable.Drawable
import android.os.Handler
import android.os.Looper
import android.view.MotionEvent
import android.view.View
import android.view.View.OnTouchListener
import com.google.android.gms.maps.model.Marker

abstract class OnInfoWindowElemTouchListener(
    private val view: View,
    private val bgDrawableNormal: Drawable?,
    private val bgDrawablePressed: Drawable?
) :
    OnTouchListener {
    private val handler = Handler(Looper.getMainLooper())
    private var marker: Marker? = null
    private var pressed = false
    fun setMarker(marker: Marker?) {
        this.marker = marker
    }

    override fun onTouch(vv: View, event: MotionEvent): Boolean {
        if (inViewArea(event.x, event.y, view.width, view.height)) {
            when (event.actionMasked) {
                MotionEvent.ACTION_DOWN -> startPress()
                MotionEvent.ACTION_UP -> handler.postDelayed(confirmClickRunnable, delayMillis)
                MotionEvent.ACTION_CANCEL -> endPress()
                else -> {}
            }
        } else {
            // If the touch goes outside of the view's area
            // (like when moving finger out of the pressed button)
            // just release the press
            endPress()
        }
        return false
    }

    private fun inViewArea(x: Float, y: Float, width: Int, height: Int): Boolean {
        return 0 <= x && x <= width && 0 <= y && y <= height
    }

    private fun startPress() {
        if (!pressed) {
            pressed = true
            handler.removeCallbacks(confirmClickRunnable)
            view.background = bgDrawablePressed
            if (marker != null) marker!!.showInfoWindow()
        }
    }

    private fun endPress(): Boolean {
        return if (pressed) {
            pressed = false
            handler.removeCallbacks(confirmClickRunnable)
            view.background = bgDrawableNormal
            if (marker != null) marker!!.showInfoWindow()
            true
        } else false
    }

    private val confirmClickRunnable = Runnable {
        if (endPress()) {
            onClickConfirmed(view, marker)
        }
    }

    /**
     * This is called after a successful click
     */
    protected abstract fun onClickConfirmed(v: View?, marker: Marker?)

    companion object {
        const val delayMillis: Long = 150
    }
}
