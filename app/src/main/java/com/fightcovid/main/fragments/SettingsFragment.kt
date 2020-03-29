package com.fightcovid.main.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.fightcovid.di.Injectable
import com.fightcovid.main.MainActivity
import com.fightcovid.signin.SignInActivity
import com.fightcovid.util.SEARCH_DISTANCE
import com.fightcovid.util.TinyDb
import com.google.fightcorona.R
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.fragment_settings.*
import javax.inject.Inject

class SettingsFragment : Fragment(), Injectable {

    @Inject
    lateinit var firebaseAuth: FirebaseAuth

    @Inject
    lateinit var tinyDb: TinyDb

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
        setupSeekbar()
    }

    private fun setupSeekbar() {

        distance_seek_bar.progress = tinyDb.getInt(SEARCH_DISTANCE, 0) - 2
        distance_selected_text.text =
            getString(R.string.selected_distance, tinyDb.getInt(SEARCH_DISTANCE, 2))

        distance_seek_bar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, p1: Int, p2: Boolean) {
                distance_selected_text.text =
                    getString(R.string.selected_distance, seekBar.progress + 2)
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {

            }

            override fun onStopTrackingTouch(seekBar: SeekBar) {
                tinyDb.putInt(SEARCH_DISTANCE, seekBar.progress + 2)
            }

        })
    }

    private fun setupUi() {
        name_value.text = firebaseAuth.currentUser?.displayName
        email_value.text = firebaseAuth.currentUser?.email
        distance_selected_text.text = getString(R.string.selected_distance, 2)
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
            tinyDb.clear()
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