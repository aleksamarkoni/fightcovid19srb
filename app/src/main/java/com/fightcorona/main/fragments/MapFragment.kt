package com.fightcorona.main.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.fightcorona.di.Injectable
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.fightcorona.R
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
        mFusedLocationProviderClient =
            LocationServices.getFusedLocationProviderClient(requireActivity())

        val map = childFragmentManager.findFragmentById(R.id.gmap_frag) as? SupportMapFragment
        map?.getMapAsync(this)
    }

    private fun updateLocationUi() {
        if (mMap == null) {
            return
        }
        try {
            if (checkPermission()) {
                mMap.isMyLocationEnabled = true
                mMap.uiSettings.isMyLocationButtonEnabled = true
                getDeviceLocation()
            } else {
                mMap.isMyLocationEnabled = false
                mMap.uiSettings.isMyLocationButtonEnabled = false
                //mLastKnownLocation = null
                //getLocationPermission()
            }
        } catch (e: SecurityException) {
            Timber.e("Exception: %s", e.message)
        }
    }

    private fun checkPermission(): Boolean {
        return true
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
}