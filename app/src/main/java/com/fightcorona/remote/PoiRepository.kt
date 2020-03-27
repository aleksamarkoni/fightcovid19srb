package com.fightcorona.remote

import com.fightcorona.main.PeopleType
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class PoiRepository(
    private val fightCorona19Service: FightCorona19RestService,
    private val retrofitUtils: RetrofitUtils
) {
    suspend fun createPointOfInterest(
        latitude: Float,
        longitude: Float,
        email: String,
        phone: String,
        name: String,
        peopleType: PeopleType,
        note: String?
    ) {
        val poi = Poi(
            peopleType.toString().toLowerCase(),
            latitude,
            longitude,
            "cara lazara",
            4,
            "1A",
            phone,
            note
        )
        val response = fightCorona19Service.createPointOfInterest(poi)
    }

    suspend fun getPoiDetail(id: Int) =
        withContext(Dispatchers.IO) {
            val response = fightCorona19Service.getPoiDetail(id)
            val result = retrofitUtils.handleResponse(response)
            return@withContext result
        }

    suspend fun getPoi(latitude: Float, longitude: Float): HashMap<MarkerOptions, Int>? =
        withContext(Dispatchers.IO) {
            val response = fightCorona19Service.getPoi(latitude, longitude)
            val result = retrofitUtils.handleResponse(response)
            return@withContext preparePoiData((result))
        }

    private fun preparePoiData(list: List<MapMarker>?): HashMap<MarkerOptions, Int> {
        val hashMap = HashMap<MarkerOptions, Int>()
        val colorList = listOf(
            BitmapDescriptorFactory.HUE_YELLOW,
            BitmapDescriptorFactory.HUE_GREEN,
            BitmapDescriptorFactory.HUE_RED
        )
        list?.let {
            for (item in list) {
                val latLng = LatLng(item.latitude.toDouble(), item.longitude.toDouble())
                hashMap[MarkerOptions().position(latLng)
                    .icon(
                        BitmapDescriptorFactory.defaultMarker(
                            BitmapDescriptorFactory.HUE_GREEN
                        )
                    )
                    .title("Marker with id ${item.id}")
                    .snippet("Click for more details")
                    .draggable(true)] = item.id
            }
        }
        return hashMap
    }
}