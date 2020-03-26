package com.fightcorona.main.fragments

import android.graphics.Bitmap
import android.graphics.Canvas
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.core.graphics.drawable.DrawableCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.fightcorona.di.Injectable
import com.fightcorona.main.MainActivity
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.fightcorona.R
import kotlinx.android.synthetic.main.fragment_person_detail.*


class EndangeredDetailFragment : Fragment(), Injectable, OnMapReadyCallback {

    private val args: EndangeredDetailFragmentArgs by navArgs()

    private lateinit var mMap: GoogleMap

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_person_detail, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setupToolbar()
        button_person_visited.text = "Visited ${args.id}"
        getMapAsync()
    }

    private fun getMapAsync() {
        val map =
            childFragmentManager.findFragmentById(R.id.map_fragment_person_detail) as? SupportMapFragment
        map?.getMapAsync(this)
    }

    private fun setupToolbar() {
        with(activity as MainActivity) {
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
            supportActionBar?.setDisplayShowTitleEnabled(true)
            setHasOptionsMenu(true)
            supportActionBar?.setHomeAsUpIndicator(
                ContextCompat.getDrawable(
                    requireContext(),
                    R.drawable.ic_arrow_back_white_24dp
                )
            )
            supportActionBar?.title = getString(R.string.visit_detail)
        }
    }

    override fun onMapReady(map: GoogleMap) {
        val latLng = LatLng(args.latitude.toDouble(), args.longitude.toDouble())
        mMap = map
        mMap.uiSettings.isMapToolbarEnabled = false
        mMap.uiSettings.isScrollGesturesEnabled = false
        mMap.addMarker(
            MarkerOptions().position(
                latLng
            ).icon(
                vectorToBitmap(
                    R.drawable.ic_home_red_24dp
                )
            )
        )
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 12f))
    }

    private fun vectorToBitmap(@DrawableRes id: Int): BitmapDescriptor? {
        val vectorDrawable =
            ResourcesCompat.getDrawable(resources, id, null)
        val bitmap = Bitmap.createBitmap(
            vectorDrawable!!.intrinsicWidth,
            vectorDrawable.intrinsicHeight, Bitmap.Config.ARGB_8888
        )
        val canvas = Canvas(bitmap)
        vectorDrawable.setBounds(0, 0, canvas.width, canvas.height)
        DrawableCompat.setTint(
            vectorDrawable,
            ContextCompat.getColor(requireContext(), R.color.red)
        )
        vectorDrawable.draw(canvas)
        return BitmapDescriptorFactory.fromBitmap(bitmap)
    }
}