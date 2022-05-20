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
import com.example.soundtoshare.recycler_view.CustomRecyclerAdapter
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
        viewModel.loadUserInfo()
        initWorkers()

        val recyclerViewItem1: RecyclerView = binding.recyclerViewItem1
        recyclerViewItem1.layoutManager = LinearLayoutManager(requireContext())
        viewModel.getObservableReactions().observe(activity as LifecycleOwner) {
                recyclerView.adapter = CustomRecyclerAdapter(it)
        }
        return binding.root

        val recyclerViewItem2: RecyclerView = binding.recyclerViewItem2
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        startUserInfoObserving()

        binding.buttonGroup.setOnCheckedChangeListener { group, checkedId ->
            when (checkedId) {
                 R.id.radioButton1 -> {
                    binding.recyclerViewItem2.visibility = View.GONE
                    binding.recyclerViewItem1.visibility = View.VISIBLE
                }
                R.id.radioButton2 -> {
                    binding.recyclerViewItem1.visibility = View.GONE
                    binding.recyclerViewItem2.visibility = View.VISIBLE
                }
            }
        }
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
//             imageLoader.displayImage(it.avatar_uri,  binding.avatar, options)
//             val fullName = it.firstName + " " + it.lastName
//             binding.fullName.text = fullName
//             binding.radioButton1.isChecked = true

//             binding.shimmer.stopShimmer()
//             binding.shimmer.visibility = View.GONE
//             binding.fullNameAndAvatarHolder.visibility = View.VISIBLE
//             binding.recyclerViewItem1.visibility = View.VISIBLE
//             binding.buttonGroup.visibility = View.VISIBLE
//            startOnLoadAnimation()
        }
    }

    companion object {

        fun newInstance(): Home {
            return Home()
        }
    }
}
