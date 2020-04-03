package com.covidvolonter.main.view_models

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.covidvolonter.remote.repository.MarkerDetails
import com.covidvolonter.remote.repository.PoiRepository
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.coroutines.launch
import timber.log.Timber
import java.io.IOException
import javax.inject.Inject

class MapViewModel @Inject constructor(private val poiRepo: PoiRepository) : ViewModel() {

    private val _mapMarkers = MutableLiveData<HashMap<MarkerOptions, MarkerDetails>>()

    val mapMarkers: LiveData<HashMap<MarkerOptions, MarkerDetails>>
        get() = _mapMarkers

    fun getPoi(latitude: Float, longitude: Float, distance: Int?) {
        viewModelScope.launch {
            try {
                _mapMarkers.value = poiRepo.getPoi(latitude, longitude, distance)
            } catch (e: IOException) {
                Timber.e(e)
            } finally {

            }
        }
    }
}