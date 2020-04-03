package com.covidvolonter.main.fragments

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.covidvolonter.di.Injectable
import com.covidvolonter.main.MainActivity
import com.covidvolonter.main.view_models.PoiDetailViewModel
import com.covidvolonter.repo.PoiDetailRepo
import com.google.fightcorona.R
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.fragement_volunteer_detail.*
import javax.inject.Inject

class VolunteerDetailFragment : Fragment(), Injectable {

    private val args: VolunteerDetailFragmentArgs by navArgs()

    @Inject
    lateinit var firebaseAuth: FirebaseAuth

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private lateinit var viewModel: PoiDetailViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragement_volunteer_detail, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this, viewModelFactory).get(PoiDetailViewModel::class.java)
        setupToolbar()
        viewModel.getPoiDetail(args.id)

        viewModel.isLoading.observe(viewLifecycleOwner, Observer { result ->
            result?.let { isLoading ->
                if (isLoading) {
                    loading_fragment_volunteer_detail.visibility = View.VISIBLE
                } else {
                    loading_fragment_volunteer_detail.visibility = View.GONE
                }
            }
        })

        viewModel.poiDetail.observe(viewLifecycleOwner, Observer { result ->
            result?.let {
                setupCallButton(it.phone)
                setupUi(it)
            }
        })
    }

    private fun setupCallButton(phone: String?) {
        call_volunteer_button.setOnClickListener {
            val intent = Intent(Intent.ACTION_DIAL)
            intent.data = Uri.parse("tel:${phone}")
            startActivity(intent)
        }
    }

    private fun setupUi(poiDetail: PoiDetailRepo) {
        name_value.text = poiDetail.poiCreator
        phone_value.text = poiDetail.phone
        loadProfileImage(poiDetail.profileImage)
    }

    private fun loadProfileImage(picture: String?) {
        Glide.with(this)
            .load(picture)
            .centerCrop()
            .placeholder(R.drawable.ic_person_purple_24dp)
            .into(volunteer_profile_image)
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
            supportActionBar?.title = getString(R.string.volunteer_details)
        }
    }
}