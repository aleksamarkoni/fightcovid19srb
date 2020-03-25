package com.fightcorona.main.view_models

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fightcorona.remote.PoiRepository
import kotlinx.coroutines.launch
import javax.inject.Inject

class AddVolunteerViewModel @Inject constructor(private val poiRepo: PoiRepository) : ViewModel() {

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean>
        get() = _isLoading

    fun addVolunteer(latitude: Float, longitude: Float) {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                poiRepo.createPointOfInterest(latitude, longitude)
            } catch (e: Exception) {

            } finally {
                _isLoading.value = false
            }
        }
    }
}