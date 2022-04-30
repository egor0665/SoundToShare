package com.example.soundtoshare.fragments.map

import android.Manifest
import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.example.soundtoshare.R
import com.example.soundtoshare.databinding.FragmentMapBinding
import com.google.android.gms.maps.SupportMapFragment

class MapFragment : Fragment() {
    private val viewModel by viewModels <MapFragmentViewModel> ()
    private lateinit var customInfoWindowAdapter: CustomInfoWindowAdapter
    private lateinit var binding: FragmentMapBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMapBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //viewModel = ViewModelProvider(this).get(MapFragmentViewModel::class.java)
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        customInfoWindowAdapter = CustomInfoWindowAdapter(requireActivity())
        viewModel.initMap(mapFragment, customInfoWindowAdapter)
        getLocationPermission()
    }

    private val permissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        viewModel.locationPermissionGranted = isGranted
        if (isGranted) {
            viewModel.updateLocationUI()
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
            viewModel.locationPermissionGranted = true
            startLocationUpdate()
        } else {
            permissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        }
    }

    private fun startLocationUpdate() {
        viewModel.getLocationData().observe(viewLifecycleOwner) {
            viewModel.cameraSetUp(it)
        }
        // Create the observer which updates the UI.
        val browserIntentObserver = Observer<Intent> { newIntent ->
            // Update the UI, in this case, a TextView.
            requireActivity().startActivity(newIntent)
        }

        // Observe the LiveData, passing in this activity as the LifecycleOwner and the observer.
        viewModel.browserIntent.observe(viewLifecycleOwner, browserIntentObserver)
    }


    private fun startDeniedPermissionAlert() {
        val alertDialogBuilder: AlertDialog.Builder = AlertDialog.Builder(requireContext())

        alertDialogBuilder.setTitle("Permission needed")
        alertDialogBuilder.setMessage("Location permission needed for accessing app")
        alertDialogBuilder.setPositiveButton(
            "Open Setting"
        ) { _, _ ->
            val intent = Intent()
            intent.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
            val uri: Uri = Uri.fromParts(
                "package",
                requireActivity().packageName,
                null
            )
            intent.data = uri
            requireActivity().startActivity(intent)
        }
        alertDialogBuilder.setNegativeButton("Cancel") { _, _ ->
            // TODO Cancel action
        }

        val dialog: AlertDialog = alertDialogBuilder.create()
        dialog.show()
    }

    companion object {
        fun newInstance(): MapFragment {
            return MapFragment()
        }
    }
}
