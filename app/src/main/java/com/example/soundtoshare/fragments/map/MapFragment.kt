package com.example.soundtoshare.fragments.map

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.soundtoshare.R
import com.example.soundtoshare.databinding.FragmentMapBinding
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import org.koin.androidx.viewmodel.ext.android.viewModel


class MapFragment : Fragment() {
    private val viewModel: MapViewModel by viewModel()
    private lateinit var binding: FragmentMapBinding
    var locationPermissionGranted = false
    private var map: GoogleMap? = null

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
    }

    override fun onResume() {
        super.onResume()
//        getLocationPermission()
        //TODO: Повторный запрос разрешений
    }

    private val requestMultiplePermissions = registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
        locationPermissionGranted = true
        permissions.entries.forEach {
            if (!it.value) locationPermissionGranted = false
        }
        if (locationPermissionGranted) {
            updateLocationUI()
            startLocationUpdate()
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
            startLocationUpdate()
        } else {
            requestMultiplePermissions.launch(arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION,
                                                      Manifest.permission.ACCESS_FINE_LOCATION))
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

    private fun startLocationUpdate() {
//        viewModel.getLocationDataViewModel().observe(viewLifecycleOwner) {
//            viewModel.cameraSetUp(it)
//        }
//        // Create the observer which updates the UI.
//        val browserIntentObserver = Observer<Intent> { newIntent ->
//            // Update the UI, in this case, a TextView.
//            requireActivity().startActivity(newIntent)
//        }
//
//        // Observe the LiveData, passing in this activity as the LifecycleOwner and the observer.
//        viewModel.browserIntent.observe(viewLifecycleOwner, browserIntentObserver)
    }

    private fun startDeniedPermissionAlert() {
        val alertDialogBuilder: AlertDialog.Builder = AlertDialog.Builder(requireContext())
        alertDialogBuilder.setTitle("Permission needed")
        alertDialogBuilder.setMessage("Location permission needed for accessing app")
        alertDialogBuilder.setPositiveButton("Open Setting") { _, _ ->
            val uri: Uri = Uri.fromParts("package", requireActivity().packageName, null)
            val intent = Intent().apply {
                action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
                data = uri
            }
            requireActivity().startActivity(intent)
        }
        alertDialogBuilder.setNegativeButton("Cancel") { _, _ ->
            //TODO: Действие при отмене запроса перехода в настройки
//            val navigator = HomeNavigator(requireActivity().supportFragmentManager,
//                                            R.id.nav_host_fragment_activity_main)
//            navigator.setScreen(Screen.Home)
        }
        val dialog: AlertDialog = alertDialogBuilder.create()
        dialog.show()
    }

    @SuppressLint("InflateParams")
    private val callback = OnMapReadyCallback{ googleMap ->
        map = googleMap
        map?.setMapStyle(
            MapStyleOptions.loadRawResourceStyle(requireContext(), R.raw.map_without_labels_style)
        )
        updateLocationUI()
        val infoWindow = layoutInflater.inflate(R.layout.custom_infowindow, null) as ViewGroup
        val customInfoWindowAdapter = CustomInfoWindowAdapter(requireActivity(), infoWindow, map)

        map?.setInfoWindowAdapter(customInfoWindowAdapter)

    }

    companion object {
        fun newInstance(): MapFragment {
            return MapFragment()
        }
    }
}
