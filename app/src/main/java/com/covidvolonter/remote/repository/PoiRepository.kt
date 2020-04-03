package com.covidvolonter.remote.repository

import com.covidvolonter.main.PeopleType
import com.covidvolonter.remote.*
import com.covidvolonter.repo.PoiDetailRepo
import com.covidvolonter.util.SEARCH_DISTANCE
import com.covidvolonter.util.TinyDb
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.*
import kotlin.collections.HashMap

class PoiRepository(
    private val fightCorona19Service: FightCorona19RestService,
    private val retrofitUtils: RetrofitUtils,
    private val tinyDb: TinyDb
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

    suspend fun createVisit(visitId: Int, feedback: String) = withContext(Dispatchers.IO) {
        val visit = Visit(visitId, feedback)
        val response = fightCorona19Service.createVisit(visit)
        //TODO add handle respone here, we can use Empty Response instead of Void
        return@withContext response.isSuccessful
    }

    suspend fun getPoiDetail(id: Int) =
        withContext(Dispatchers.IO) {
            val response = fightCorona19Service.getPoiDetail(id)
            val result = retrofitUtils.handleResponse(response)
            return@withContext PoiDetailRepo.map(result)
        }

    suspend fun getPoi(
        latitude: Float,
        longitude: Float,
        distance: Int?
    ): HashMap<MarkerOptions, MarkerDetails>? =
        withContext(Dispatchers.IO) {
            val response =
                fightCorona19Service.getPoi(
                    latitude, longitude,
                    distance ?: tinyDb.getInt(SEARCH_DISTANCE, 2)
                )
            val result = retrofitUtils.handleResponse(response)
            return@withContext preparePoiData((result))
        }

    private fun preparePoiData(list: List<MapMarker>?): HashMap<MarkerOptions, MarkerDetails> {
        val hashMap = HashMap<MarkerOptions, MarkerDetails>()
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
                        //TODO ovo citati is ResourceProvider
                    .title(if (item.type == PeopleType.ENDANGERED.name.toLowerCase(Locale.getDefault())) "Endangered person" else "Volunteer")
                    .snippet("Click for more details")
                    .draggable(true)] =
                    MarkerDetails(item.id, PeopleType.valueOf(item.type.toUpperCase()))
            }
        }
        return hashMap
    }
}

data class MarkerDetails(
    val id: Int,
    val markerType: PeopleType
)