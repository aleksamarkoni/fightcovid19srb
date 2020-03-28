package com.fightcorona.remote

import com.fightcorona.main.PeopleType
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.*
import kotlin.collections.HashMap

class PoiRepository(
    private val fightCorona19Service: FightCorona19RestService,
    private val retrofitUtils: RetrofitUtils
) {
    suspend fun createPointOfInterest(
        latitude: Float,
        longitude: Float,
        email: String?,
        phone: String?,
        name: String,
        peopleType: PeopleType,
        note: String?,
        address: String,
        apartment: String,
        floor: String?
    ) = withContext(Dispatchers.IO) {
        val poi = Poi(
            peopleType.toString().toLowerCase(),
            latitude,
            longitude,
            address,
            if (floor != "") floor?.toInt() else null,
            apartment,
            phone,
            note
        )
        val response = fightCorona19Service.createPointOfInterest(poi)
        return@withContext response.isSuccessful
    }

    suspend fun getPoiDetail(id: Int) =
        withContext(Dispatchers.IO) {
            val response = fightCorona19Service.getPoiDetail(id)
            return@withContext retrofitUtils.handleResponse(response)
        }

    suspend fun getPoi(latitude: Float, longitude: Float): HashMap<MarkerOptions, Int>? =
        withContext(Dispatchers.IO) {
            val response = fightCorona19Service.getPoi(latitude, longitude, null)
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
                        if (item.type == PeopleType.ENDANGERED.name.toLowerCase(Locale.getDefault()))
                            BitmapDescriptorFactory.defaultMarker(
                                BitmapDescriptorFactory.HUE_GREEN
                            ) else BitmapDescriptorFactory.defaultMarker(
                            BitmapDescriptorFactory.HUE_BLUE
                        )
                    )
                    .title(if (item.type == PeopleType.ENDANGERED.name.toLowerCase(Locale.getDefault())) "Endangered person" else "Volunteer")
                    .snippet("Click for more details")
                    .draggable(true)] = item.id
            }
        }
        return hashMap
    }
}