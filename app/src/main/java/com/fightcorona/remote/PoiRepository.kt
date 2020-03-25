package com.fightcorona.remote

import android.location.Location
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class PoiRepository(
    private val fightCorona19Service: FightCorona19RestService
) {
    suspend fun createPointOfInterest(latitude: Float, longitude: Float) {
        val response = fightCorona19Service.createPointOfInterest(latitude, longitude)
    }

    fun fetchMarkers(location: Location): LiveData<List<MarkerOptions>> {
        return createMockMarkers(location)
    }

    private fun createMockMarkers(location: Location): LiveData<List<MarkerOptions>> {
        val liveData = MutableLiveData<List<MarkerOptions>>()

        val list = mutableListOf<MarkerOptions>()
        var minus = 0.010f
        val colorList = listOf(
            BitmapDescriptorFactory.HUE_AZURE,
            BitmapDescriptorFactory.HUE_BLUE,
            BitmapDescriptorFactory.HUE_CYAN,
            BitmapDescriptorFactory.HUE_RED,
            BitmapDescriptorFactory.HUE_ROSE
        )

        for (i in 0 until 10) {
            val latLng = LatLng(location.latitude - minus, location.longitude + minus)
            list.add(
                MarkerOptions().position(latLng)
                    .icon(BitmapDescriptorFactory.defaultMarker(colorList[(0..4).random()]))
                    .draggable(true)
            )
            minus -= 0.010f
        }
        liveData.value = list
        return liveData
    }
}