package com.volontero.main.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.volontero.di.Injectable
import com.volontero.main.MainActivity
import com.volontero.main.view_models.SettingsViewModel
import com.volontero.signin.SignInActivity
import com.volontero.util.LogoutError
import com.volontero.util.LogoutResult
import com.volontero.util.SEARCH_DISTANCE
import com.volontero.util.TinyDb
import com.google.firebase.auth.FirebaseAuth
import com.volontero.R
import kotlinx.android.synthetic.main.fragment_settings.*
import javax.inject.Inject

class SettingsFragment : Fragment(), Injectable {

    @Inject
    lateinit var firebaseAuth: FirebaseAuth

    @Inject
    lateinit var tinyDb: TinyDb

    @Inject
    lateinit var viewModelProvider: ViewModelProvider.Factory

    private lateinit var viewModel: SettingsViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_settings, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this, viewModelProvider).get(SettingsViewModel::class.java)
        setupToolbar()
        setupLogoutButton()
        setupUi()
        setupSeekbar()

        viewModel.isLoading.observe(viewLifecycleOwner, Observer { result ->
            result?.let { isLoading ->
                if (isLoading) {
                    logout_button.visibility = View.INVISIBLE
                    logout_progress_spinner.show()
                } else {
                    logout_progress_spinner.hide()
                    logout_button.visibility = View.VISIBLE
                }
            }
        })

        viewModel.logoutState.observe(viewLifecycleOwner, Observer { result ->
            result?.let { logoutResult ->
                when (logoutResult) {
                    LogoutResult -> {
                        startActivity(SignInActivity.createIntent(requireContext()))
                        activity?.finish()
                    }
                    is LogoutError -> {
                        Toast.makeText(
                            requireContext(),
                            "${logoutResult.error}",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        })
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
            viewModel.logoutUser()
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