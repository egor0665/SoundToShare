package com.example.soundtoshare.fragments.sign_in

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.soundtoshare.R
import com.example.soundtoshare.apis.GoogleAuth
import com.example.soundtoshare.databinding.FragmentSignInBinding
import com.example.soundtoshare.fragments.home.HomeFragment

class SignInFragment: Fragment() {
    //private lateinit var binding: FragmentSignInBinding
    //val launcher = GoogleAuth.SignInLauncher.newInstance(this)
    //2
    companion object {

        fun newInstance(): SignInFragment {
            return SignInFragment()
        }
    }

    //3
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_sign_in, container, false)


    }
}

