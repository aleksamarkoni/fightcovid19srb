package com.covidvolonter.main.view_models

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.covidvolonter.remote.repository.PoiRepository
import kotlinx.coroutines.launch
import java.io.IOException
import javax.inject.Inject

class CreateVisitViewModel @Inject constructor(private val poiRepository: PoiRepository) :
    ViewModel() {

    private val _isLoading = MutableLiveData<Boolean>()

    private val _createVisitSuccess = MutableLiveData<Boolean>()

    val createVisitSuccess: LiveData<Boolean>
        get() = _createVisitSuccess

    val isLoading: LiveData<Boolean>
        get() = _isLoading

    fun createVisit(poiId: Int, feedback: String) {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                _createVisitSuccess.value = poiRepository.createVisit(poiId, feedback)
            } catch (e: IOException) {
                _createVisitSuccess.value = false
            } finally {
                _isLoading.value = false
            }
        }
    }
}