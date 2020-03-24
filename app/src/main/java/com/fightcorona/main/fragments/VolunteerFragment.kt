package com.fightcorona.main.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.fightcorona.di.Injectable
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.fightcorona.R

class VolunteerFragment : BottomSheetDialogFragment(), Injectable {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_volunteer, container, false)
    }

    override fun getTheme(): Int = R.style.BottomSheetDialogTheme
}