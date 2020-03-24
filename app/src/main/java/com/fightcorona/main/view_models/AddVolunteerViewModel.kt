package com.fightcorona.main.view_models

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fightcorona.remote.PoiRepository
import kotlinx.coroutines.launch
import javax.inject.Inject

class AddVolunteerViewModel @Inject constructor(private val poiRepo: PoiRepository) : ViewModel() {
    fun addVolunteer() {
        viewModelScope.launch {
            poiRepo.createPointOfInterest()
        }
    }
}