package com.example.soundtoshare.fragments


import android.util.Log
import com.example.soundtoshare.BuildConfig
import com.spotify.sdk.android.auth.AuthorizationRequest
import com.spotify.sdk.android.auth.AuthorizationResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONException
import org.json.JSONObject
import java.net.URL
import javax.net.ssl.HttpsURLConnection

object SpotifyAPI {
    public fun getAuthenticationRequest(): AuthorizationRequest {
        return AuthorizationRequest.Builder(BuildConfig.KEY, AuthorizationResponse.Type.TOKEN, BuildConfig.URI)
            .setScopes(arrayOf("user-read-currently-playing"))
            .build()
    }

    public fun fetchSpotifyMusic(token: String?) {
        if (token != null) {
            GlobalScope.launch(Dispatchers.Default) {
                val httpsURLConnection =
                    withContext(Dispatchers.IO) { URL("https://api.spotify.com/v1/me/player/currently-playing").openConnection() as HttpsURLConnection }
                httpsURLConnection.requestMethod = "GET"
                httpsURLConnection.setRequestProperty("Authorization", "Bearer $token")
                httpsURLConnection.doInput = true
                httpsURLConnection.doOutput = false
                val response = httpsURLConnection.inputStream.bufferedReader()
                    .use { it.readText() }  // defaults to UTF-8
                withContext(Dispatchers.Main) {
                    try {
                        val jsonObject = JSONObject(response)
                        val spotifyMusic: String =
                            jsonObject.getJSONObject("item").getString("name")
                        Log.d("Music:", spotifyMusic)
                    } catch (e: JSONException) {
                        Log.d("Music:", "Does not play")
                    }
                }
            }

        }
    }
}