package com.example.soundtoshare.fragments.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.soundtoshare.MainActivity
import com.example.soundtoshare.databinding.FragmentSignInBinding

class HomeFragment : Fragment() {
    private lateinit var binding: FragmentSignInBinding
    private val viewModel: HomeFragmentViewModel by activityViewModels()
    //2
    companion object {

        fun newInstance(): HomeFragment {
            return HomeFragment()
        }
    }

    //3
    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = FragmentSignInBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.spotify.setOnClickListener{
            val activity = requireActivity() as MainActivity
            viewModel.signInSpotify(activity,activity.launcher)
        }
    }

}