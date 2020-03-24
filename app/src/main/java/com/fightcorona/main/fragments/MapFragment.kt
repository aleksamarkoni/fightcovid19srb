package com.fightcorona.main.fragments

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.fightcorona.di.Injectable
import com.fightcorona.main.MainActivity
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.fightcorona.R
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import kotlinx.android.synthetic.main.fragment_map.*
import timber.log.Timber

class MapFragment : Fragment(), Injectable, OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var mFusedLocationProviderClient: FusedLocationProviderClient

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_map, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val activity = activity as MainActivity
        activity.supportActionBar?.setDisplayHomeAsUpEnabled(false)
        activity.supportActionBar?.setDisplayShowTitleEnabled(true)
        getLocationPermissions()
        mFusedLocationProviderClient =
            LocationServices.getFusedLocationProviderClient(requireActivity())
        setupAddVolunteerButton()
    }

    private fun setupAddVolunteerButton() {
        add_volunteer_button.setOnClickListener {
            findNavController().navigate(MapFragmentDirections.actionMapFragmentToChooseAddressFragment())
        }
    }

    fun getMapAsync() {
        val map = childFragmentManager.findFragmentById(R.id.gmap_frag) as? SupportMapFragment
        map?.getMapAsync(this)
    }

    private fun updateLocationUi() {
        if (mMap == null) {
            return
        }
        try {
            if (arePermissionsGranted()) {
                mMap.isMyLocationEnabled = true
                mMap.uiSettings.isMyLocationButtonEnabled = true
                getDeviceLocation()
            } else {
                mMap.isMyLocationEnabled = false
                mMap.uiSettings.isMyLocationButtonEnabled = false
                getLocationPermissions()
                //mLastKnownLocation = null
                //getLocationPermission()
            }
        } catch (e: SecurityException) {
            Timber.e("Exception: %s", e.message)
        }
    }

    private fun getDeviceLocation() {
        val locationResult = mFusedLocationProviderClient.lastLocation
        locationResult.addOnSuccessListener(requireActivity()) {
            if (it != null) {
                //lastLocation = it
                val currentLatLng = LatLng(it.latitude, it.longitude)
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, 12f))
            }
        }
    }


    override fun onMapReady(map: GoogleMap) {
        mMap = map
        updateLocationUi()
    }

    private fun getLocationPermissions() {
        Dexter.withActivity(requireActivity())
            .withPermissions(
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION
            )
            .withListener(object : MultiplePermissionsListener {

                override fun onPermissionRationaleShouldBeShown(
                    permissions: MutableList<com.karumi.dexter.listener.PermissionRequest>?,
                    token: PermissionToken?
                ) {
                    token?.continuePermissionRequest()
                }

                override fun onPermissionsChecked(report: MultiplePermissionsReport) {
                    when {
                        report.areAllPermissionsGranted() -> {
                            getMapAsync()
                        }
                        report.isAnyPermissionPermanentlyDenied -> showPermissionSettingsDialog()
                        else -> showPermissionSettingsDialog()
                    }
                }
            }).check()
    }

    private fun showPermissionSettingsDialog() {
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle(getString(R.string.permission_needed))
        builder.setMessage(getString(R.string.permission_dialog_desc))
        builder.setCancelable(false)
        builder.setPositiveButton(getString(R.string.go_to_settings)) { dialog, which ->
            dialog.dismiss()
            val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
            intent.data = Uri.parse("package:" + requireContext().packageName)
            startActivity(intent)
        }
        builder.setNegativeButton(getString(R.string.cancel_button)) { dialog, which ->
            dialog.dismiss()
            activity?.finish()
        }
        val dialog = builder.create()
        dialog.show()
    }

    private fun arePermissionsGranted(): Boolean {
        return ContextCompat.checkSelfPermission(
            requireContext(),
            Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(
                    requireContext(),
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED
    }
}