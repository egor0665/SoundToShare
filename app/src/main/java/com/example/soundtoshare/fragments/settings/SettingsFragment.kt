package com.example.soundtoshare.fragments.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.soundtoshare.MainActivity
import com.example.soundtoshare.R
import com.example.soundtoshare.databinding.FragmentHomeBinding
import com.example.soundtoshare.databinding.FragmentSettingsBinding
import com.example.soundtoshare.fragments.home.HomeFragment
import com.example.soundtoshare.fragments.home.HomeFragmentViewModel
import com.example.soundtoshare.fragments.map.MapFragment
import com.vk.api.sdk.VK
import com.vk.api.sdk.auth.VKScope

class SettingsFragment: Fragment(){
    private lateinit var binding: FragmentSettingsBinding

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        binding = FragmentSettingsBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.buttonSignOut.setOnClickListener{
            (this.activity as MainActivity).vk_signout()
        }
    }
    companion object {

        fun newInstance(): SettingsFragment {
            return SettingsFragment()
        }
    }
}