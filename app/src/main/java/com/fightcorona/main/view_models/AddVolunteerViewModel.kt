package com.fightcorona.main.view_models

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fightcorona.main.PeopleType
import com.fightcorona.remote.PoiRepository
import kotlinx.coroutines.launch
import javax.inject.Inject

class AddVolunteerViewModel @Inject constructor(private val poiRepo: PoiRepository) : ViewModel() {

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean>
        get() = _isLoading

    fun addNewPerson(
        latitude: Float,
        longitude: Float,
        email: String,
        phone: String,
        name: String,
        peopleType: PeopleType,
        note: String?
    ) {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                poiRepo.createPointOfInterest(
                    latitude,
                    longitude,
                    email,
                    phone,
                    name,
                    peopleType,
                    note
                )
            } catch (e: Exception) {

            } finally {
                _isLoading.value = false
            }
        }
    }
}