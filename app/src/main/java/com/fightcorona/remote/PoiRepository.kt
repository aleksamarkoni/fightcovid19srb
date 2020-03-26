package com.fightcorona.remote

import android.location.Location
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

    fun fetchMarkers(location: Location): MutableLiveData<HashMap<MarkerOptions, Int>> {
        return createMockMarkers(location)
    }

    private fun createMockMarkers(location: Location): MutableLiveData<HashMap<MarkerOptions, Int>> {
        val liveDataHashMap = MutableLiveData<HashMap<MarkerOptions, Int>>()
        val hashMap = HashMap<MarkerOptions, Int>()
        var minus = 0.010f
        val colorList = listOf(
            BitmapDescriptorFactory.HUE_YELLOW,
            BitmapDescriptorFactory.HUE_GREEN,
            BitmapDescriptorFactory.HUE_RED
        )

        for (i in 0 until 10) {
            val latLng = LatLng(location.latitude - minus, location.longitude + minus)
            hashMap[MarkerOptions().position(latLng)
                .icon(BitmapDescriptorFactory.defaultMarker(colorList[(0..2).random()]))
                .title("Baba i deda")
                .snippet("TEstiramo da vidimo sta radi ovo cudo")
                .draggable(true)] = (0..20).random()
            minus -= 0.010f
        }
        liveDataHashMap.value = hashMap
        return liveDataHashMap
    }
}