package com.fightcovid.main.fragments

import android.Manifest
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.view.*
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.fightcovid.di.Injectable
import com.fightcovid.main.MainActivity
import com.fightcovid.main.PeopleType.ENDANGERED
import com.fightcovid.main.PeopleType.VOLUNTEER
import com.fightcovid.main.fragments.MapType.*
import com.fightcovid.main.view_models.MapViewModel
import com.fightcovid.remote.repository.MarkerDetails
import com.fightcovid.util.MAP_TYPE
import com.fightcovid.util.TinyDb
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.fightcorona.R
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import kotlinx.android.synthetic.main.fragment_map.*
import timber.log.Timber
import javax.inject.Inject

class MapFragment : Fragment(), Injectable, OnMapReadyCallback {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    @Inject
    lateinit var tinyDb: TinyDb

    private lateinit var viewModel: MapViewModel

    private lateinit var mMap: GoogleMap
    private lateinit var mFusedLocationProviderClient: FusedLocationProviderClient
    private var markerHashMap = HashMap<Marker, MarkerDetails>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_map, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this, viewModelFactory).get(MapViewModel::class.java)
        setupToolbar()
        getLocationPermissions()
        mFusedLocationProviderClient =
            LocationServices.getFusedLocationProviderClient(requireActivity())
        setupAddVolunteerButton()
        setupAddEndangeredButton()
        setupChooseMapButton()
    }

    private fun setupChooseMapButton() {
        choose_map_type_button.setOnClickListener {
            findNavController().navigate(MapFragmentDirections.actionMapFragmentToChooseMapTypeFragment())
        }
    }

    private fun setupToolbar() {
        with(activity as MainActivity) {
            supportActionBar?.setDisplayHomeAsUpEnabled(false)
            supportActionBar?.setDisplayShowTitleEnabled(true)
            setHasOptionsMenu(true)
            supportActionBar?.title = getString(R.string.app_name)
        }
    }

    private fun setupAddVolunteerButton() {
        add_volunteer_button.setOnClickListener {
            findNavController().navigate(
                MapFragmentDirections.actionMapFragmentToChooseAddressFragment(
                    VOLUNTEER
                )
            )
        }
    }

    private fun setupAddEndangeredButton() {
        add_endangered_people_people.setOnClickListener {
            findNavController().navigate(
                MapFragmentDirections.actionMapFragmentToChooseAddressFragment(
                    ENDANGERED
                )
            )
        }
    }

    fun getMapAsync() {
        val map = childFragmentManager.findFragmentById(R.id.gmap_frag) as? SupportMapFragment
        map?.getMapAsync(this)
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
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, 16f))
                viewModel.getPoi(it.latitude.toFloat(), it.longitude.toFloat())
            }
        }
    }


    override fun onMapReady(map: GoogleMap) {
        mMap = map
        val mapType = tinyDb.getString(MAP_TYPE)
        setMapType(if (mapType.isNullOrBlank()) SATELLITE_MAP else valueOf(mapType))
        mMap.uiSettings.isMapToolbarEnabled = false
        updateLocationUi()
        mMap.setOnInfoWindowClickListener { marker ->
            getMarkerDetail(marker)
        }
        viewModel.mapMarkers.observe(this, Observer { marker ->
            mMap.clear()
            marker?.let { hashMap ->
                setupMarkers(hashMap)
            }
        })
    }

    private fun getMarkerDetail(marker: Marker) {
        Timber.d("Id of clicked marker is ${markerHashMap[marker]}")
        markerHashMap[marker]?.let { markerDetail ->
            when (markerDetail.markerType) {
                VOLUNTEER -> {
                    openVolunteerDetailFragment(markerDetail.id)
                }
                ENDANGERED -> {
                    openEndangeredDetailFragment(marker, markerDetail.id)
                }
            }
        }
    }

    private fun openVolunteerDetailFragment(id: Int) {
        findNavController().navigate(
            MapFragmentDirections.actionMapFragmentToVolunteerDetailFragment(id)
        )
    }

    private fun openEndangeredDetailFragment(
        marker: Marker,
        endangeredId: Int
    ) {
        findNavController().navigate(
            MapFragmentDirections.actionMapFragmentToEndangeredDetailFragment2(
                endangeredId,
                marker.position.latitude.toFloat(),
                marker.position.longitude.toFloat()
            )
        )
    }

    private fun setupMarkers(hashMap: HashMap<MarkerOptions, MarkerDetails>) {
        for (item in hashMap.keys) {
            val mapMarker = mMap.addMarker(item)
            markerHashMap[mapMarker] = hashMap[item]!!
        }
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

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        menu.clear()
        inflater.inflate(R.menu.settings_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.settings -> {
                openSettings()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun openSettings() {
        findNavController().navigate(MapFragmentDirections.actionMapFragmentToSettingsFragment())
    }

    fun setMapType(mapType: MapType) {
        when (mapType) {
            DEFAULT_MAP -> mMap.mapType = GoogleMap.MAP_TYPE_NORMAL
            SATELLITE_MAP -> mMap.mapType = GoogleMap.MAP_TYPE_HYBRID
            TERRAIN_MAP -> mMap.mapType = GoogleMap.MAP_TYPE_TERRAIN
        }
    }
}