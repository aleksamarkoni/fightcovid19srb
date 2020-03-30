package com.fightcovid.main.fragments

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
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.fightcovid.di.Injectable
import com.fightcovid.main.MainActivity
import com.fightcovid.main.view_models.PersonDetailViewModel
import com.fightcovid.remote.PoiDetail
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
import javax.inject.Inject


class EndangeredDetailFragment : Fragment(), Injectable, OnMapReadyCallback {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private lateinit var viewModel: PersonDetailViewModel

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
        viewModel = ViewModelProvider(this, viewModelFactory).get(PersonDetailViewModel::class.java)
        setupToolbar()
        getMapAsync()
        setupViewAllFeedbacksButton()
        setupAddVisitButton()
        fetchPoiDetail()

        viewModel.poiDetail.observe(viewLifecycleOwner, Observer { poiDetail ->
            poiDetail?.let {
                setupUi(poiDetail)
            }
        })
    }

    private fun setupAddVisitButton() {
        button_person_visited.setOnClickListener {
            findNavController().navigate(
                EndangeredDetailFragmentDirections.actionEndangeredDetailFragment2ToCreateVisitFragment(
                    args.id
                )
            )
        }
    }

    private fun setupUi(poiDetail: PoiDetail) {
        notes_value.text = poiDetail.note
        address_value.text =
            getString(R.string.format_address, poiDetail.address, poiDetail.apartment)
    }

    private fun fetchPoiDetail() {
        viewModel.getPoiDetail(args.id)
    }

    private fun setupViewAllFeedbacksButton() {
        view_all_feedbacks.setOnClickListener {
            findNavController().navigate(EndangeredDetailFragmentDirections.actionEndangeredDetailFragment2ToAllFeedbacksFragment())
        }
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
        mMap.uiSettings.isZoomGesturesEnabled = false
        mMap.uiSettings.isScrollGesturesEnabled = false
        mMap.addMarker(
            MarkerOptions().position(
                latLng
            ).icon(
                vectorToBitmap(
                    R.drawable.ic_home_red_24dp
                )
            ).draggable(false)
        )
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 16f))
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