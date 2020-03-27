package com.fightcorona.main.view_models

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fightcorona.remote.PoiRepository
import kotlinx.coroutines.launch
import timber.log.Timber
import java.io.IOException
import javax.inject.Inject

class PersonDetailViewModel @Inject constructor(private val poiRepository: PoiRepository) :
    ViewModel() {

    fun getPoiDetail(id: Int) {
        viewModelScope.launch {
            try {
                poiRepository.getPoiDetail(id)
            } catch (e: IOException) {
                Timber.e(e)
            }
        }
    }


}