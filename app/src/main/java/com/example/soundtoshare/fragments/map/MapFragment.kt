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
import androidx.lifecycle.ViewModelProvider
import com.example.soundtoshare.R
import com.google.android.gms.maps.SupportMapFragment

class MapFragment : Fragment() {
    private lateinit var vmBinding: MapFragmentViewModel

    companion object {
        fun newInstance(): MapFragment {
            return MapFragment()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        vmBinding = ViewModelProvider(this).get(MapFragmentViewModel::class.java)
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        vmBinding.initMap(mapFragment)
        getLocationPermission()
    }

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_map, container, false)
    }

    private val permissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        vmBinding.locationPermissionGranted = isGranted
        if (isGranted) {
            vmBinding.updateLocationUI()
            startLocationUpdate()
        }
        else {
            startDeniedPermissionAlert()
        }
    }

    private fun getLocationPermission() {
        if (ContextCompat.checkSelfPermission(requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION)
            == PackageManager.PERMISSION_GRANTED) {
            vmBinding.locationPermissionGranted = true
            startLocationUpdate()
        }
        else {
            permissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        }
    }

    private fun startLocationUpdate() {
        vmBinding.getLocationData().observe(viewLifecycleOwner) {
            vmBinding.moveCamera(it)
        }
    }

    private fun startDeniedPermissionAlert() {
        val alertDialogBuilder: AlertDialog.Builder = AlertDialog.Builder(requireContext())

        alertDialogBuilder.setTitle("Permission needed")
        alertDialogBuilder.setMessage("Location permission needed for accessing app")
        alertDialogBuilder.setPositiveButton("Open Setting"
        ) { _, _ ->
            val intent = Intent()
            intent.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
            val uri: Uri = Uri.fromParts(
                "package", requireActivity().packageName,
                null
            )
            intent.data = uri
            requireActivity().startActivity(intent)
        }
        alertDialogBuilder.setNegativeButton("Cancel") { _, _ ->
            //TODO Cancel action
        }

        val dialog: AlertDialog = alertDialogBuilder.create()
        dialog.show()
    }

}