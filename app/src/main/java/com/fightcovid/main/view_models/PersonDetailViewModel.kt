package com.fightcovid.main.view_models

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fightcovid.remote.PoiDetail
import com.fightcovid.remote.repository.PoiRepository
import kotlinx.coroutines.launch
import timber.log.Timber
import java.io.IOException
import javax.inject.Inject

class PersonDetailViewModel @Inject constructor(private val poiRepository: PoiRepository) :
    ViewModel() {

    private val _poiDetail = MutableLiveData<PoiDetail>()

    val poiDetail: LiveData<PoiDetail>
        get() = _poiDetail

    fun getPoiDetail(id: Int) {
        viewModelScope.launch {
            try {
                _poiDetail.value = poiRepository.getPoiDetail(id)
            } catch (e: IOException) {
                Timber.e(e)
            }
        }
    }


}