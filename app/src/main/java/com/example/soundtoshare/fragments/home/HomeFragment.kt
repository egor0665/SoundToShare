package com.example.soundtoshare.fragments.home

import android.graphics.*
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.LifecycleOwner
import com.example.soundtoshare.databinding.FragmentHomeBinding


class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding
    private val viewModel: HomeFragmentViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.initViewModel()
        viewModel.userInfo.observe(activity as LifecycleOwner) {
            val avatar = it.avatar
            val output = Bitmap.createBitmap(avatar.width, avatar.height, Bitmap.Config.ARGB_8888)
            val canvas = Canvas(output)
            val paint = Paint()
            val rect = Rect(0, 0, avatar.width, avatar.height)
            val rectF = RectF(Rect(0, 0, avatar.width, avatar.height))
            paint.isAntiAlias = true
            canvas.drawARGB(0, 0, 0, 0)
            paint.color = -0xbdbdbe
            canvas.drawRoundRect(rectF, binding.avatar.height.toFloat(), binding.avatar.height.toFloat(), paint)
            paint.xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_IN)
            canvas.drawBitmap(avatar, rect, rect, paint)
            binding.avatar.setImageBitmap(output)
            binding.firstName.text = it.firstName
            binding.lastName.text = it.lastName
        }
    }
    companion object {

        fun newInstance(): HomeFragment {
            return HomeFragment()
        }
    }
}
