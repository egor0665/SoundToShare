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
        viewModel.getUserInfo()
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
        ObservableUserSongInfo.getUserInfoLiveData().observe(activity as LifecycleOwner) {
            val avatar = it.avatar
            val output = Bitmap.createBitmap(avatar.width, avatar.height, Bitmap.Config.ARGB_8888)
            val canvas = Canvas(output)
            val paint = Paint()
            val rect = Rect(0, 0, avatar.width, avatar.height)
            val rectF = RectF(Rect(0, 0, avatar.width, avatar.height))
            paint.isAntiAlias = true
            canvas.drawARGB(0, 0, 0, 0)
            paint.color = -0xbdbdbe
            canvas.drawRoundRect(
                rectF,
                binding.avatar.height.toFloat(),
                binding.avatar.height.toFloat(),
                paint
            )
            paint.xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_IN)
            canvas.drawBitmap(avatar, rect, rect, paint)
            binding.avatar.setImageBitmap(output)

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
