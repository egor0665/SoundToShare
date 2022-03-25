package com.example.soundtoshare.apis

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import kotlinx.android.synthetic.main.activity_main.*
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityOptionsCompat
import androidx.lifecycle.MutableLiveData
import com.example.soundtoshare.R
import com.example.soundtoshare.databinding.ActivityMainBinding
import com.example.soundtoshare.databinding.FragmentHomeBinding
import com.example.soundtoshare.databinding.FragmentSignInBinding
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.identity.Identity
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.components.BuildConfig
import com.google.firebase.ktx.Firebase
import com.spotify.sdk.android.auth.AuthorizationClient
import java.lang.Exception

object GoogleAuth {
    private const val EMAIL_LETTERS_COUNT = "@gmail.com".length
    private const val TAG = "GOOGLE_SIGN_IN_TAG"

    private var _gso: GoogleSignInOptions? = null
    private fun getGso(activity: AppCompatActivity): GoogleSignInOptions {
        val localGso = _gso ?: GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(activity.getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        _gso = localGso
        return localGso
    }

    private val firebaseAuth = FirebaseAuth.getInstance()

    fun signIn(activity: AppCompatActivity, launcher: SignInLauncher? = null, errorCallback: (() -> Unit)? = null) {
        Log.d(TAG, "signIn: before silent signIn")
        val localGoogleSignInClient = GoogleSignIn.getClient(
            activity.applicationContext,
            getGso(activity)
        )
        val signInTask = localGoogleSignInClient.silentSignIn()
            .addOnSuccessListener { account ->
                Log.d(TAG, "Successful google silent signIn")
                //firebaseSignIn(account, activity)
            }
        if (launcher != null) {
            signInTask.addOnFailureListener {
                Log.d(TAG, "Starting google signIn intent...")
                launcher.launch(localGoogleSignInClient.signInIntent)
            }
        }
        if (errorCallback != null) {
            signInTask.addOnFailureListener {
                errorCallback()
            }.addOnCanceledListener {
                errorCallback()
            }
        }
    }

    fun silentSignIn(activity: AppCompatActivity, errorCallback: (() -> Unit)? = null) {
        signIn(activity, errorCallback = errorCallback)
    }

    fun signOut(activity: AppCompatActivity) {
        //firestoreSignOut()

        firebaseAuth.signOut()

        val gso = getGso(activity)

        GoogleSignIn.getClient(activity.applicationContext, gso)
            .signOut()

        //_currentUser.postValue(null)
    }

    fun googleSignInFromIntent(intent: Intent?, activity: AppCompatActivity) {
        // Get data from intent
        val task = GoogleSignIn.getSignedInAccountFromIntent(intent)
        task
            .addOnSuccessListener { /* account -> firebaseSignIn(account, activity)*/ }
            .addOnFailureListener {
               // _errorMessage.postValue("Authentication via Google failed")
               // _currentUser.postValue(null)
                signOut(activity)
            }
    }

    class SignInLauncher private constructor(
        private val launcher: ActivityResultLauncher<Intent>
    ) : ActivityResultLauncher<Intent>() {
        override fun launch(input: Intent?, options: ActivityOptionsCompat?) {
            launcher.launch(input, options)
        }

        override fun unregister() {
            launcher.unregister()
        }

        override fun getContract(): ActivityResultContract<Intent, *> {
            return launcher.contract
        }

        companion object {
            fun newInstance(activity: AppCompatActivity): SignInLauncher {
                val launcher = activity.registerForActivityResult(
                    ActivityResultContracts.StartActivityForResult()
                ) { result ->
                    if (result.resultCode != Activity.RESULT_OK) {
                        Log.w(TAG, "Google signIn intent failure")
                        //_errorMessage.postValue("Intent failure")
                        //_currentUser.postValue(null)
                        return@registerForActivityResult
                    }
                    Log.d(TAG, "Successful google signIn intent")
                    googleSignInFromIntent(result.data, activity)
                }

                return SignInLauncher(launcher)
            }
        }
    }

}


