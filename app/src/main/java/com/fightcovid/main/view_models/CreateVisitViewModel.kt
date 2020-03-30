package com.fightcovid.main.view_models

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fightcovid.remote.repository.PoiRepository
import kotlinx.coroutines.launch
import java.io.IOException
import javax.inject.Inject

class CreateVisitViewModel @Inject constructor(private val poiRepository: PoiRepository) :
    ViewModel() {

    fun createVisit(poiId: Int, feedback: String) {
        viewModelScope.launch {
            try {
                poiRepository.createVisit(poiId, feedback)
            } catch (e: IOException) {

            }
        }
    }
}