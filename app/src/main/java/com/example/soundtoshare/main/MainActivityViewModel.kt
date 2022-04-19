package com.example.soundtoshare.main

import android.content.Context
import androidx.lifecycle.ViewModel

class MainActivityViewModel(context: Context): ViewModel()  {
    private val sharedPreferences = SharedPreferences(context)
    fun getIncognitoMode():Boolean{
        return sharedPreferences.getIncognitoMode()
    }

    fun setIncognitoMode(mode: Boolean) {
        sharedPreferences.setIncognitoMode(mode)
    }
}

