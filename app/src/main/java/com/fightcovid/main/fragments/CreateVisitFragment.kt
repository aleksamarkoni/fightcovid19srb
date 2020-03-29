package com.fightcovid.main.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import com.fightcovid.di.Injectable
import com.fightcovid.main.view_models.CreateVisitViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.fightcorona.R
import kotlinx.android.synthetic.main.fragment_create_visit.*
import javax.inject.Inject

class CreateVisitFragment : BottomSheetDialogFragment(), Injectable {

    private val args: CreateVisitFragmentArgs by navArgs()

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private lateinit var viewModel: CreateVisitViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_create_visit, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this, viewModelFactory).get(CreateVisitViewModel::class.java)
        setupCreateVisitButton()
    }

    private fun setupCreateVisitButton() {
        create_visit_button.setOnClickListener {
            viewModel.createVisit(args.poiId, visit_detail_edit_text.text.toString())
        }
    }
}