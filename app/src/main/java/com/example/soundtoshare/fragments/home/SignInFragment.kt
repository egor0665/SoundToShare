package com.example.soundtoshare.fragments.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.soundtoshare.MainActivity
import com.example.soundtoshare.databinding.FragmentSignInBinding
import com.vk.api.sdk.auth.VKScope

class SignInFragment: Fragment() {
    private lateinit var binding: FragmentSignInBinding
    private val viewModel: HomeFragmentViewModel by activityViewModels()

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        binding = FragmentSignInBinding.inflate(inflater)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
7
        viewModel.initViewModel()
//        VK.setConfig(VKApiConfig((this.activity as MainActivity).applicationContext, BuildConfig.vk_id.toInt()))
        binding.buttonSignIn.setOnClickListener{
            viewModel.signInVK((this.activity as MainActivity).authVkLauncher, arrayListOf(VKScope.STATUS))
        }
    }
    companion object {

        fun newInstance(): HomeFragment {
            return HomeFragment()
        }
    }

}