package com.fightcovid.main.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.fightcovid.di.Injectable
import com.fightcovid.main.MainActivity
import com.google.fightcorona.R
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.fragement_volunteer_detail.*
import javax.inject.Inject

class VolunteerDetailFragment : Fragment(), Injectable {

    @Inject
    lateinit var firebaseAuth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragement_volunteer_detail, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setupToolbar()
        setupUi()
    }

    private fun setupUi() {
        name_value.text = firebaseAuth.currentUser?.displayName
        loadProfileImage()
    }

    private fun loadProfileImage() {
        Glide.with(this)
            .load(firebaseAuth.currentUser?.photoUrl)
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