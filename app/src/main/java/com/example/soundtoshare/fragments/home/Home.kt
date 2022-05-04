package com.example.soundtoshare.fragments.home

import android.graphics.*
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager
import com.example.soundtoshare.databinding.FragmentHomeBinding
import com.example.soundtoshare.recycler_view.CustomRecyclerAdapter
import com.example.soundtoshare.repositories.Reaction
import com.example.soundtoshare.workers.VkWorker
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
        viewModel.loadUserInfo()
        initWorkers()

        val recyclerView: RecyclerView = binding.recyclerView
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        viewModel.getObservableReactions().observe(activity as LifecycleOwner) {
                recyclerView.adapter = CustomRecyclerAdapter(it)
        }
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
            binding.fullNameAndAvatarHolder.visibility = View.VISIBLE
//            startOnLoadAnimation()
        }
    }

//    private fun fillList(): List<String> {
//        val data = mutableListOf<String>()
//        (0..30).forEach { i -> data.add("$i element") }
//        return data
//    }


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
