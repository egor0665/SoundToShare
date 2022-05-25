package com.example.soundtoshare.fragments.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.soundtoshare.databinding.FragmentSignInBinding
import com.example.soundtoshare.main.MainActivity
import com.vk.api.sdk.auth.VKScope
import org.koin.androidx.viewmodel.ext.android.viewModel

class SignIn : Fragment() {

    private lateinit var binding: FragmentSignInBinding

    private val viewModel: HomeViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSignInBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.buttonSignIn.setOnClickListener() {
            viewModel.signInVK(
                (activity as MainActivity).authVkLauncher,
                arrayListOf(VKScope.STATUS, VKScope.OFFLINE)
            )
        }
    }

    companion object {

        fun newInstance(): Home {
            return Home()
        }
    }
}
