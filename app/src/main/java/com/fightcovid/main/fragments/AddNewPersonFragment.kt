package com.fightcovid.main.fragments

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.fightcovid.di.Injectable
import com.fightcovid.main.PeopleType
import com.fightcovid.main.view_models.AddPersonViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.fightcorona.R
import kotlinx.android.synthetic.main.fragment_add_person.*
import timber.log.Timber
import javax.inject.Inject

class AddNewPersonFragment : BottomSheetDialogFragment(), Injectable {

    private val args: AddNewPersonFragmentArgs by navArgs()

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private lateinit var viewModel: AddPersonViewModel

    private val watcher = object : TextWatcher {
        override fun afterTextChanged(p0: Editable?) {
            button_add_volunteer.isEnabled = address_edit_text.text.toString().isNotEmpty() &&
                    apartment_edit_text.text.toString().isNotEmpty() &&
                    name_edit_text.text.toString().isNotEmpty()
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
        return inflater.inflate(R.layout.fragment_add_person, container, false)
    }

    override fun getTheme(): Int = R.style.BottomSheetDialogTheme

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this, viewModelFactory).get(AddPersonViewModel::class.java)
        setupAddVolunteerButton()
        setupUI()
        apartment_edit_text.addTextChangedListener(watcher)
        address_edit_text.addTextChangedListener(watcher)
        name_edit_text.addTextChangedListener(watcher)

        viewModel.isLoading.observe(viewLifecycleOwner, Observer { isLoading ->
            if (isLoading) {
                button_add_volunteer.visibility = View.INVISIBLE
                create_person_progress.show()
            } else {
                create_person_progress.hide()
                button_add_volunteer.visibility = View.VISIBLE
            }
        })

        viewModel.poiCreated.observe(viewLifecycleOwner, Observer { created ->
            if (created) {
                findNavController().popBackStack(R.id.mapFragment, false)
            } else {
                Timber.e("Error")
            }
        })
    }

    private fun setupUI() {
        if (args.peopleType == PeopleType.ENDANGERED) {
            add_volunteer_title.text =
                getString(R.string.add_endangered)
            add_volunteer_desc.text =
                getString(R.string.to_add_new_endangered)
        } else {
            add_volunteer_title.text = getString(R.string.add_volunteer)
            add_volunteer_desc.text =
                getString(R.string.to_add_new_volunteer)
        }
    }

    private fun setupAddVolunteerButton() {
        button_add_volunteer.setOnClickListener {
            viewModel.addNewPerson(
                args.latitude,
                args.longitude,
                email_edit_text.text.toString(),
                phone_edit_text.text.toString(),
                name_edit_text.text.toString(),
                args.peopleType,
                notes_edit_text.text.toString(),
                apartment_edit_text.text.toString(),
                address_edit_text.text.toString(),
                floor_edit_text.text.toString()
            )
        }
    }
}