package com.example.soundtoshare.fragments.home

import android.content.Intent
import androidx.activity.result.ActivityResultLauncher
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import com.example.soundtoshare.apis.SpotifyAPI
import com.spotify.sdk.android.auth.AuthorizationClient

class HomeFragmentViewModel: ViewModel() {
    fun signInSpotify(activity: AppCompatActivity, launcher: ActivityResultLauncher<Intent>) {
        launcher.launch(AuthorizationClient.createLoginActivityIntent(activity, SpotifyAPI.getAuthenticationRequest()))
    }
    // Add Data Here
}
    // Add Functions Here

