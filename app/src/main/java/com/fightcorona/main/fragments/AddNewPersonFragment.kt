package com.fightcorona.main.fragments

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import com.fightcorona.di.Injectable
import com.fightcorona.main.PeopleType
import com.fightcorona.main.view_models.AddVolunteerViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.fightcorona.R
import kotlinx.android.synthetic.main.fragment_volunteer.*
import javax.inject.Inject

class AddNewPersonFragment : BottomSheetDialogFragment(), Injectable {

    private val args: AddNewPersonFragmentArgs by navArgs()

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private lateinit var viewModel: AddVolunteerViewModel

    private val watcher = object : TextWatcher {
        override fun afterTextChanged(p0: Editable?) {
            button_add_volunteer.isEnabled = email_edit_text.text.toString().isNotEmpty() &&
                    phone_edit_text.text.toString().isNotEmpty() &&
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
        return inflater.inflate(R.layout.fragment_volunteer, container, false)
    }

    override fun getTheme(): Int = R.style.BottomSheetDialogTheme

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this, viewModelFactory).get(AddVolunteerViewModel::class.java)
        setupAddVolunteerButton()
        setupUI()
        email_edit_text.addTextChangedListener(watcher)
        phone_edit_text.addTextChangedListener(watcher)
        name_edit_text.addTextChangedListener(watcher)
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
            viewModel.addVolunteer(args.latitude, args.longitude)
        }
    }
}