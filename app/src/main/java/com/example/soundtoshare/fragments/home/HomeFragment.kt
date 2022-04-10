package com.example.soundtoshare.fragments.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.LifecycleOwner
import com.example.soundtoshare.MainActivity
import com.example.soundtoshare.databinding.FragmentSidnInVkBinding
import com.vk.api.sdk.auth.VKScope

class HomeFragment : Fragment() {
    private lateinit var binding: FragmentSidnInVkBinding
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
                              savedInstanceState: Bundle?): View {
        binding = FragmentSidnInVkBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.initViewModel()

        binding.buttonSignIn.setOnClickListener{
            val activity = requireActivity() as MainActivity
            viewModel.signInVK(activity.authVkLauncher, arrayListOf(VKScope.STATUS))
        }

        viewModel.bitmapAvatar.observe(activity as LifecycleOwner) {
            binding.questionView.setImageBitmap(it)
        }
    }

}