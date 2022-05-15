package com.example.soundtoshare.fragments.home

import android.graphics.*
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager
import com.example.soundtoshare.databinding.FragmentHomeBinding
import com.example.soundtoshare.databinding.ShimmerLayoutBinding
import com.example.soundtoshare.external.ObservableUserSongInfo
import com.example.soundtoshare.workers.VkWorker
import com.facebook.shimmer.ShimmerFrameLayout
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.concurrent.TimeUnit

class Home : Fragment() {
    private lateinit var binding: FragmentHomeBinding
    private val viewModel: HomeViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentHomeBinding.inflate(inflater)
        binding.shimmer.startShimmer()
        viewModel.loadUserInfo()
        initWorkers()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        startUserInfoObserving()
    }

    private fun initWorkers() {
        WorkManager.getInstance(requireContext())
            .enqueue(
                OneTimeWorkRequest.Builder(VkWorker::class.java)
                    .addTag("VKMusic")
                    .setInitialDelay(10, TimeUnit.SECONDS)
                    .build()
            )
    }
    private fun startUserInfoObserving() {

        viewModel.getUserInfoLiveData().observe(activity as LifecycleOwner) {
            binding.avatar.setImageBitmap(it.avatar)
            val fullName = it.firstName + " " + it.lastName
            binding.fullName.text = fullName

            binding.shimmer.stopShimmer()
            binding.shimmer.visibility = View.GONE
            binding.fullNameAndAvatarHolder.visibility = View.VISIBLE
//            startOnLoadAnimation()


        }
    }

//    private fun startOnLoadAnimation() {
//        val animation: Animation =
//            AnimationUtils.loadAnimation(this.requireContext(), R.anim.default_animation)
//        binding.fullNameAndAvatarHolder.startAnimation(animation)
//        binding.fullNameAndAvatarHolder.visibility = View.VISIBLE
//    }

    companion object {

        fun newInstance(): Home {
            return Home()
        }
    }
}
