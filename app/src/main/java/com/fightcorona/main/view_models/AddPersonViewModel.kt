package com.fightcorona.main.view_models

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fightcorona.main.PeopleType
import com.fightcorona.remote.PoiRepository
import kotlinx.coroutines.launch
import timber.log.Timber
import java.io.IOException
import javax.inject.Inject

class AddPersonViewModel @Inject constructor(private val poiRepo: PoiRepository) : ViewModel() {

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean>
        get() = _isLoading

    private val _poiCreated = MutableLiveData<Boolean>()

    val poiCreated: LiveData<Boolean>
        get() = _poiCreated

    fun addNewPerson(
        latitude: Float,
        longitude: Float,
        email: String?,
        phone: String?,
        name: String,
        peopleType: PeopleType,
        note: String?,
        apartment: String,
        address: String,
        floor: String?
    ) {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                _poiCreated.value = poiRepo.createPointOfInterest(
                    latitude,
                    longitude,
                    email,
                    phone,
                    name,
                    peopleType,
                    note,
                    address,
                    apartment,
                    floor
                )
            } catch (e: IOException) {
                Timber.e(e)
            } finally {
                _isLoading.value = false
            }
        }
    }
}