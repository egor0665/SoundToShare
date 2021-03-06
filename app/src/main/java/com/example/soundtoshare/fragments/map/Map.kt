package com.example.soundtoshare.fragments.map

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import com.example.soundtoshare.R
import com.example.soundtoshare.databinding.FragmentMapBinding
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.MapStyleOptions
import org.koin.androidx.viewmodel.ext.android.viewModel

class Map : Fragment() {
    private val viewModel: MapViewModel by viewModel()
    private lateinit var binding: FragmentMapBinding
    private var googleMap: GoogleMap? = null
    private val handler = Handler(Looper.getMainLooper())
    private lateinit var runnableCode: Runnable

    @SuppressLint("InflateParams", "MissingPermission")
    private val callback = OnMapReadyCallback { googleMap ->
        this.googleMap = googleMap
        val infoWindow = layoutInflater.inflate(R.layout.custom_info_window, null) as ViewGroup
        val customInfoWindowAdapter = CustomInfoWindowAdapter(
            requireActivity(),
            infoWindow,
            this.googleMap
        )
        this.googleMap?.apply {
            isMyLocationEnabled = true
            uiSettings.isMyLocationButtonEnabled = true
            setMapStyle(
                MapStyleOptions.loadRawResourceStyle(
                    requireContext(),
                    R.raw.map_without_labels_style
                )
            )
            setInfoWindowAdapter(customInfoWindowAdapter)
            setOnCameraIdleListener(viewModel)
            setOnMarkerClickListener(viewModel.updateMarkersUseCase)
            setOnInfoWindowClickListener(viewModel.updateMarkersUseCase)
        }
        viewModel.moveCameraUseCase.initUseCase(this.googleMap!!)
        viewModel.updateMarkersUseCase.initUseCase(this.googleMap!!)
        customInfoWindowAdapter.getObservableButtonClicked().observe(activity as LifecycleOwner) {
            when (it.second) {
                1 -> viewModel.likeSong(it.first)
                2 -> startActivity(
                    Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse(
                            "https://m.vk.com/audio?q=" + it.first.song + "-" + it.first.artist
                        )
                    )
                )
            }
        }
    }

    private fun initLoad() {
        runnableCode = object : Runnable {
            override fun run() {
                viewModel.onCameraIdle()
                handler.postDelayed(this, handlerDelay)
            }
        }
        handler.post(runnableCode)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMapBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment: SupportMapFragment? = childFragmentManager.findFragmentById(R.id.map)
            as SupportMapFragment?
        mapFragment?.getMapAsync(callback)
        initLoad()
        onStartLocationUpdate()
    }

    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)
        if (!hidden) {
            handler.post(runnableCode)
        } else {
            handler.removeCallbacks(runnableCode)
        }
    }

    override fun onResume() {
        super.onResume()
        handler.post(runnableCode)
    }
    override fun onPause() {
        super.onPause()
        handler.removeCallbacks(runnableCode)
    }

    private fun onStartLocationUpdate() {
        viewModel.startLocationUpdate().observe(viewLifecycleOwner) {
            viewModel.cameraSetUp(it)
        }
    }

    companion object {
        fun newInstance(): Map {
            return Map()
        }

        const val handlerDelay: Long = 20000
    }
}
