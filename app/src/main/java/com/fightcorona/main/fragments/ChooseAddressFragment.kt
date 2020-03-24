package com.fightcorona.main.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
import com.google.android.gms.maps.model.MarkerOptions
import com.google.fightcorona.R
import kotlinx.android.synthetic.main.fragment_choose_address.*
import timber.log.Timber

class ChooseAddressFragment : Fragment(), Injectable, OnMapReadyCallback,
    GoogleMap.OnCameraMoveListener,
    GoogleMap.OnCameraIdleListener {

    private lateinit var mMap: GoogleMap
    private lateinit var mFusedLocationProviderClient: FusedLocationProviderClient

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_choose_address, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setupToolbar()
        setupContinueButton()
        setupCancelButton()
        getMapAsync()

        mFusedLocationProviderClient =
            LocationServices.getFusedLocationProviderClient(requireActivity())
    }

    private fun getMapAsync() {
        val map =
            childFragmentManager.findFragmentById(R.id.choose_address_map) as? SupportMapFragment
        map?.getMapAsync(this)
    }

    private fun setupCancelButton() {
        cancel_button_fragment_choose_address.setOnClickListener {
            activity?.onBackPressed()
        }
    }

    private fun setupContinueButton() {
        continue_button_fragment_choose_address.setOnClickListener {
            findNavController().navigate(ChooseAddressFragmentDirections.actionChooseAddressFragmentToVolunteerFragment())
        }
    }

    private fun setupToolbar() {
        val activity = activity as MainActivity
        activity.supportActionBar?.setDisplayHomeAsUpEnabled(true)
        activity.supportActionBar?.setDisplayShowTitleEnabled(false)
        activity.supportActionBar?.setHomeAsUpIndicator(
            ContextCompat.getDrawable(
                requireContext(),
                R.drawable.ic_arrow_back_white_24dp
            )
        )
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        mMap.apply {
            setOnCameraMoveListener(this@ChooseAddressFragment)
            setOnCameraIdleListener(this@ChooseAddressFragment)
        }
        updateLocationUi()
    }

    private fun updateLocationUi() {
        try {
            if (true) {
                mMap.isMyLocationEnabled = true
                mMap.uiSettings.isMyLocationButtonEnabled = true
                getDeviceLocation()
            } else {
                mMap.isMyLocationEnabled = false
                mMap.uiSettings.isMyLocationButtonEnabled = false
                //getLocationPermissions()
                //mLastKnownLocation = null
            }
        } catch (e: SecurityException) {
            Timber.e("Exception: %s", e.message)
        }
    }

    private fun getDeviceLocation() {
        val locationResult = mFusedLocationProviderClient.lastLocation
        locationResult.addOnSuccessListener(requireActivity()) {
            if (it != null) {
                val currentLatLng = LatLng(it.latitude, it.longitude)
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, 12f))
            }
        }
    }

    override fun onCameraMove() {
        mMap.clear()
        location_pin.visibility = View.VISIBLE
    }

    override fun onCameraIdle() {
        location_pin.visibility = View.GONE

        val markerOptions = MarkerOptions().position(mMap.cameraPosition.target)
            .draggable(true)
        Timber.d("Marker droped on ${mMap.cameraPosition.target}")
        mMap.addMarker(markerOptions)

    }
}