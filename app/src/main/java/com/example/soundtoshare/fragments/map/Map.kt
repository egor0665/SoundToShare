package com.example.soundtoshare.fragments.map

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import com.example.soundtoshare.R
import com.example.soundtoshare.databinding.FragmentMapBinding
import com.example.soundtoshare.main.MainActivity
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import org.koin.androidx.viewmodel.ext.android.viewModel


class Map : Fragment() {
    private val viewModel: MapViewModel by viewModel()
    private lateinit var binding: FragmentMapBinding
    private var locationPermissionGranted = false
    private var map: GoogleMap? = null
    private val handler = Handler(Looper.getMainLooper())
    private lateinit var runnableCode: Runnable

    @SuppressLint("InflateParams")
    private val callback = OnMapReadyCallback{ googleMap ->
        map = googleMap
        updateLocationUI()
        val infoWindow = layoutInflater.inflate(R.layout.custom_info_window, null) as ViewGroup
        val customInfoWindowAdapter = CustomInfoWindowAdapter(requireActivity(), infoWindow, map)

        map?.apply {
            setMapStyle(MapStyleOptions.loadRawResourceStyle(requireContext(), R.raw.map_without_labels_style))
            setInfoWindowAdapter(customInfoWindowAdapter)
            setOnCameraIdleListener(viewModel)
            setOnMarkerClickListener(viewModel.updateMarkersUseCase)
        }
        viewModel.moveCameraUseCase.initUseCase(map!!)
        viewModel.updateMarkersUseCase.initUseCase(map!!)
        customInfoWindowAdapter.getObservableButtonClicked().observe(activity as LifecycleOwner) {
            when (it.second) {
                1-> viewModel.likeSong(it.first)
                2->startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://m.vk.com/audio?q="+it.first.song+"-"+ it.first.artist)))
            }
        }
    }

    private fun initLoad() {
         runnableCode = object : Runnable {
            override fun run() {
                viewModel.onCameraIdle()
                handler.postDelayed(this, 20000)
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
        getLocationPermission()
        val mapFragment: SupportMapFragment? = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(callback)
        initLoad()
    }

    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)
        if (!hidden) {
            getLocationPermission()
            handler.post(runnableCode)
        }
        else {
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

    private val requestMultiplePermissions = registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
        locationPermissionGranted = true
        permissions.entries.forEach {
            if (!it.value) locationPermissionGranted = false
        }
        if (locationPermissionGranted) {
            updateLocationUI()
            onStartLocationUpdate()
        } else {
            startDeniedPermissionAlert()
        }
    }


    private fun getLocationPermission() {
        if (ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            )
            == PackageManager.PERMISSION_GRANTED
        ) {
            locationPermissionGranted = true
            onStartLocationUpdate()
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                requestMultiplePermissions.launch(arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION,
                                                          Manifest.permission.ACCESS_FINE_LOCATION,
                                                          Manifest.permission.ACCESS_BACKGROUND_LOCATION))
            }
            else {
                requestMultiplePermissions.launch(arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_FINE_LOCATION))
            }
        }
    }

    @SuppressLint("MissingPermission")
    fun updateLocationUI() {
        if (locationPermissionGranted) {
            map?.isMyLocationEnabled = true
            map?.uiSettings?.isMyLocationButtonEnabled = true
        } else {
            map?.isMyLocationEnabled = false
            map?.uiSettings?.isMyLocationButtonEnabled = false
        }
    }

    private fun onStartLocationUpdate() {
        viewModel.startLocationUpdate().observe(viewLifecycleOwner) {
            viewModel.cameraSetUp(it)
        }
    }

    private fun startDeniedPermissionAlert() {
        val alertDialogBuilder: AlertDialog.Builder = AlertDialog.Builder(requireContext()).apply {
            setTitle("Permission needed")
            setMessage("Location permission needed for app to work")
            setPositiveButton("Open Setting") { _, _ ->
                val uri: Uri = Uri.fromParts("package", requireActivity().packageName, null)
                val intent = Intent().apply {
                    action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
                    data = uri
                }
                requireActivity().startActivity(intent)
            }
            setNegativeButton("Cancel") { _, _ ->
                startActivity(Intent(requireContext(), MainActivity::class.java))
            }
        }

        val dialog: AlertDialog = alertDialogBuilder.create()
        dialog.show()
    }

    companion object {
        fun newInstance(): Map {
            return Map()
        }
    }
}
