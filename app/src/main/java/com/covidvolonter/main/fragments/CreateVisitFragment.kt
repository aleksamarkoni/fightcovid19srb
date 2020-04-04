package com.covidvolonter.main.fragments

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import com.facebook.FacebookSdk.getApplicationContext
import com.covidvolonter.di.Injectable
import com.covidvolonter.main.view_models.CreateVisitViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.fightcorona.R
import kotlinx.android.synthetic.main.fragment_create_visit.*
import javax.inject.Inject

class CreateVisitFragment : BottomSheetDialogFragment(), Injectable {

    private val args: CreateVisitFragmentArgs by navArgs()

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private lateinit var viewModel: CreateVisitViewModel

    private val watcher = object : TextWatcher {
        override fun afterTextChanged(p0: Editable?) {
            create_visit_button.isEnabled = visit_detail_edit_text.text.toString().isNotEmpty()
        }

        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
        }

        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
        }
    }

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
        visit_detail_edit_text.addTextChangedListener(watcher)

        viewModel.isLoading.observe(viewLifecycleOwner, Observer { result ->
            result?.let { isLoading ->
                if (isLoading) {
                    create_visit_progress.show()
                    create_visit_button.visibility = View.INVISIBLE
                } else {
                    create_visit_progress.hide()
                    create_visit_button.visibility = View.VISIBLE
                }
            }
        })

        //TOdo open main map fragment on post visit success
        viewModel.createVisitSuccess.observe(viewLifecycleOwner, Observer { result ->
            result?.let { createVisitSuccess ->
                if (createVisitSuccess) {
                    Toast.makeText(
                        getApplicationContext(),
                        getString(R.string.visit_created),
                        Toast.LENGTH_SHORT
                    )
                        .show()
                    dismiss()
                } else {
                    Toast.makeText(
                        getApplicationContext(),
                        getString(R.string.error_creating_visit),
                        Toast.LENGTH_SHORT
                    )
                        .show()
                }
            }
        })
    }

    private fun setupCreateVisitButton() {
        create_visit_button.setOnClickListener {
            viewModel.createVisit(args.poiId, visit_detail_edit_text.text.toString())
        }
    }
}