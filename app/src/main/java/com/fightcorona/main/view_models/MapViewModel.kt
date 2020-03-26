package com.fightcorona.main.view_models

import android.location.Location
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fightcorona.remote.PoiRepository
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

class MapViewModel @Inject constructor(private val poiRepo: PoiRepository) : ViewModel() {

    private val currentLocation = MutableLiveData<Location>()

    private val _mapMarkers = MutableLiveData<HashMap<MarkerOptions, Int>>()

    val mapMarkers: LiveData<HashMap<MarkerOptions, Int>>
        get() = _mapMarkers

    fun setLocation(it: Location) {
        currentLocation.value = it
    }

    fun getPoi(latitude: Float, longitude: Float) {
        viewModelScope.launch {
            try {
                _mapMarkers.value = poiRepo.getPoi(latitude, longitude)
            } catch (e: Exception) {
                Timber.e(e)
            } finally {

            }
        }
    }
}