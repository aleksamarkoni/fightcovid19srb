package com.fightcorona.main.view_models

import android.location.Location
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.fightcorona.remote.PoiRepository
import com.google.android.gms.maps.model.MarkerOptions
import javax.inject.Inject

class MapViewModel @Inject constructor(private val poiRepo: PoiRepository) : ViewModel() {

    private val currentLocation = MutableLiveData<Location>()

    val mapMarkers: LiveData<List<MarkerOptions>> =
        Transformations.switchMap(currentLocation) { location ->
            location?.let {
                poiRepo.fetchMarkers(it)
            }
        }

    private fun prepareData(it: Location) {

    }

    fun setLocation(it: Location) {
        currentLocation.value = it
    }
}