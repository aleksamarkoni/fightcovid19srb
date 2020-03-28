package com.fightcorona.main.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.fightcorona.di.Injectable
import com.fightcorona.main.MainActivity
import com.fightcorona.signin.SignInActivity
import com.google.fightcorona.R
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.fragment_settings.*
import javax.inject.Inject

class SettingsFragment : Fragment(), Injectable {

    @Inject
    lateinit var firebaseAuth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_settings, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setupToolbar()
        setupLogoutButton()
        setupUi()
    }

    private fun setupUi() {
        name_value.text = firebaseAuth.currentUser?.displayName
        email_value.text = firebaseAuth.currentUser?.email
        setupProfilePicture()
    }

    private fun setupProfilePicture() {
        Glide.with(this)
            .load(firebaseAuth.currentUser?.photoUrl)
            .centerCrop()
            .placeholder(R.drawable.ic_person_purple_24dp)
            .into(profile_image)
    }

    private fun setupLogoutButton() {
        logout_button.setOnClickListener {
            firebaseAuth.signOut()
            startActivity(SignInActivity.createIntent(requireContext()))
            activity?.finish()
        }
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
            supportActionBar?.title = getString(R.string.settings)
        }
    }
}