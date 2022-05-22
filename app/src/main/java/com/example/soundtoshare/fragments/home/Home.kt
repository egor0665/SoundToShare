package com.example.soundtoshare.fragments.home

import android.graphics.Bitmap
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager
import com.example.soundtoshare.R
import com.example.soundtoshare.databinding.FragmentHomeBinding
import com.example.soundtoshare.recycler_view.RecyclerAdapterLikedSongs
import com.example.soundtoshare.recycler_view.RecyclerAdapterReactions
import com.example.soundtoshare.repositories.Reaction
import com.example.soundtoshare.workers.VkWorker
import com.nostra13.universalimageloader.core.DisplayImageOptions
import com.nostra13.universalimageloader.core.ImageLoader
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration
import com.nostra13.universalimageloader.core.assist.FailReason
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener
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
        binding.shimmerReyclerView.startShimmer()
        viewModel.loadUserInfo(){
            binding.shimmerReyclerView.stopShimmer()
            binding.radioButton1.isChecked = true
            binding.shimmerReyclerView.visibility = View.GONE
            binding.recyclerView1.visibility = View.VISIBLE
            binding.buttonGroup.visibility = View.VISIBLE
        }
        initWorkers()
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        startUserInfoObserving()
        setUpRecyclers()
        binding.buttonGroup.setOnCheckedChangeListener { group, checkedId ->
            when (checkedId) {
                 R.id.radioButton1 -> {
                     viewModel.addLikedSong()
                    binding.recyclerView2.visibility = View.GONE
                    binding.recyclerView1.visibility = View.VISIBLE
                }
                R.id.radioButton2 -> {
                    binding.recyclerView1.visibility = View.GONE
                    binding.recyclerView2.visibility = View.VISIBLE
                }
            }
        }
    }

    private fun setUpRecyclers() {
        val recyclerView1: RecyclerView = binding.recyclerView1
        val recyclerView2: RecyclerView = binding.recyclerView2
        var linearLayoutManager1 = LinearLayoutManager(requireContext())
        var linearLayoutManager2 = LinearLayoutManager(requireContext())
        linearLayoutManager1.apply {
            reverseLayout = true
            stackFromEnd = true
        }
        linearLayoutManager2.apply {
            reverseLayout = true
            stackFromEnd = true
        }
        recyclerView1.layoutManager = linearLayoutManager1
        recyclerView2.layoutManager = linearLayoutManager2
        viewModel.getObservableReactions().observe(activity as LifecycleOwner) {
            recyclerView1.adapter = RecyclerAdapterReactions(it)
        }
        viewModel.getObservableLikedSongs().observe(activity as LifecycleOwner) {
            recyclerView2.adapter = RecyclerAdapterLikedSongs(it)
        }
        viewModel.loadLikedSongs()
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

    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)
        if (!hidden) viewModel.loadLikedSongs()
    }
    private fun startUserInfoObserving() {
        viewModel.getUserInfoLiveData().observe(activity as LifecycleOwner) {
            val options =
                DisplayImageOptions.Builder().displayer(RoundedBitmapDisplayer(360)).build()
            val imageLoader = ImageLoader.getInstance()
            imageLoader.init(ImageLoaderConfiguration.createDefault(activity))
            imageLoader.displayImage(
                it.avatar_uri,
                binding.avatar,
                options,
                object : ImageLoadingListener {
                    override fun onLoadingStarted(imageUri: String?, view: View?) {
                    }

                    override fun onLoadingFailed(
                        imageUri: String?,
                        view: View?,
                        failReason: FailReason?
                    ) {
                    }

                    override fun onLoadingComplete(
                        imageUri: String?,
                        view: View?,
                        loadedImage: Bitmap?
                    ) {
                        val fullName = it.firstName + " " + it.lastName
                        binding.fullName.text = fullName
                        binding.shimmer.stopShimmer()
                        binding.shimmer.visibility = View.GONE
                        binding.fullNameAndAvatarHolder.visibility = View.VISIBLE
                    }
                    override fun onLoadingCancelled(imageUri: String?, view: View?) {
                    }
                })
        }
    }

    companion object {

        fun newInstance(): Home {
            return Home()
        }
    }
}
