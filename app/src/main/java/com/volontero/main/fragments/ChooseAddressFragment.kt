package com.volontero.main.fragments

import android.os.Bundle
import android.view.*
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.volontero.di.Injectable
import com.volontero.main.MainActivity
import com.volontero.main.PeopleType
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.volontero.R
import kotlinx.android.synthetic.main.fragment_choose_address.*
import timber.log.Timber

class ChooseAddressFragment : Fragment(), Injectable, OnMapReadyCallback,
    GoogleMap.OnCameraMoveListener,
    GoogleMap.OnCameraIdleListener {

    private val args: ChooseAddressFragmentArgs by navArgs()

    private lateinit var mMap: GoogleMap
    private lateinit var mFusedLocationProviderClient: FusedLocationProviderClient
    private var lat = -1f
    private var lon = -1f

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
            findNavController().navigate(
                ChooseAddressFragmentDirections.actionChooseAddressFragmentToVolunteerFragment(
                    lat,
                    lon,
                    args.peopleType
                )
            )
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        menu.clear()
        inflater.inflate(R.menu.person_detail_menu, menu)
    }

    private fun setupToolbar() {
        with(activity as MainActivity) {
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
            supportActionBar?.setDisplayShowTitleEnabled(true)
            setHasOptionsMenu(false)
            supportActionBar?.setHomeAsUpIndicator(
                ContextCompat.getDrawable(
                    requireContext(),
                    R.drawable.ic_arrow_back_white_24dp
                )
            )
            if (args.peopleType == PeopleType.ENDANGERED) supportActionBar?.title =
                getString(R.string.add_endangered) else supportActionBar?.title =
                getString(R.string.add_volunteer)
        }
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
            mMap.isMyLocationEnabled = true
            mMap.uiSettings.isMyLocationButtonEnabled = true
            getDeviceLocation()
        } catch (e: SecurityException) {
            Timber.e("Exception: %s", e.message)
        }
    }

    private fun getDeviceLocation() {
        val locationResult = mFusedLocationProviderClient.lastLocation
        locationResult.addOnSuccessListener(requireActivity()) {
            if (it != null) {
                val currentLatLng = LatLng(it.latitude, it.longitude)
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, 16f))
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
        lat = mMap.cameraPosition.target.latitude.toFloat()
        lon = mMap.cameraPosition.target.longitude.toFloat()
        mMap.addMarker(markerOptions)

    }
}