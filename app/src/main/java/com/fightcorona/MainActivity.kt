package com.fightcorona

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.fightcorona.R
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasAndroidInjector
import kotlinx.android.synthetic.main.activity_main.*
import javax.inject.Inject


class MainActivity : AppCompatActivity(), OnMapReadyCallback, HasAndroidInjector {

    private lateinit var mMap: GoogleMap
    private lateinit var mFusedLocationProviderClient: FusedLocationProviderClient

    @Inject
    lateinit var dispatchingAndroidInjector: DispatchingAndroidInjector<Any>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        val map = map as SupportMapFragment
        map.getMapAsync(this)
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
            Log.e("Exception: %s", e.message)
        }
    }

    private fun checkPermission(): Boolean {
        return true
    }

    private fun getDeviceLocation() {
        val locationResult = mFusedLocationProviderClient.lastLocation
        locationResult.addOnSuccessListener(this) {
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

    override fun androidInjector(): AndroidInjector<Any> {
        return dispatchingAndroidInjector
    }
}
