package com.volontero.remote.repository

import com.volontero.main.PeopleType
import com.volontero.remote.*
import com.volontero.repo.NoNotes
import com.volontero.repo.NotesRepo
import com.volontero.repo.PoiDetailRepo
import com.volontero.util.DateTimeUtil
import com.volontero.util.SEARCH_DISTANCE
import com.volontero.util.TinyDb
import com.volontero.util.resource_proivder.ResourceProvider
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.threeten.bp.LocalDateTime
import org.threeten.bp.temporal.ChronoUnit
import java.util.*
import kotlin.collections.HashMap

class PoiRepository(
    private val volonteroService: VolonteroRestService,
    private val retrofitUtils: RetrofitUtils,
    private val tinyDb: TinyDb,
    private val resourceProvider: ResourceProvider
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
        val response = volonteroService.createPointOfInterest(poi)
        return@withContext response.isSuccessful
    }

    suspend fun createVisit(visitId: Int, feedback: String) = withContext(Dispatchers.IO) {
        val visit = Visit(visitId, feedback, true)
        val response = volonteroService.createVisit(visit)
        val result = retrofitUtils.handleResponse(response)
        return@withContext response.isSuccessful
    }

    suspend fun getPoiDetail(id: Int) =
        withContext(Dispatchers.IO) {
            val response = volonteroService.getPoiDetail(id)
            val result = retrofitUtils.handleResponse(response)
            return@withContext PoiDetailRepo.map(result)
        }

    suspend fun getNotesForPoi(poidId: Int) =
        withContext(Dispatchers.IO) {
            val response = volonteroService.getNotesForPoi(poidId)
            val result = retrofitUtils.handleResponse(response)
            val listOfNotesRepo = mutableListOf<com.volontero.repo.NoteType>()
            if (result.isEmpty()) {
                listOfNotesRepo.add(NoNotes())
            } else {
                for (item in result) {
                    listOfNotesRepo.add(NotesRepo.map(item))
                }
            }
            return@withContext listOfNotesRepo
        }

    suspend fun getPoi(
        latitude: Float,
        longitude: Float,
        distance: Int?
    ): HashMap<MarkerOptions, MarkerDetails>? =
        withContext(Dispatchers.IO) {
            val response =
                volonteroService.getPoi(
                    latitude, longitude,
                    distance ?: tinyDb.getInt(SEARCH_DISTANCE, 2)
                )
            val result = retrofitUtils.handleResponse(response)
            return@withContext preparePoiData((result))
        }

    private fun preparePoiData(list: List<MapMarker>?): HashMap<MarkerOptions, MarkerDetails> {
        val hashMap = HashMap<MarkerOptions, MarkerDetails>()
        list?.let {
            for (item in list) {
                val latLng = LatLng(item.latitude.toDouble(), item.longitude.toDouble())
                hashMap[MarkerOptions().position(latLng)
                    .icon(
                        if (item.type == PeopleType.ENDANGERED.name.toLowerCase(Locale.getDefault()))
                            checkMapMarkerColor(item.lastVisit) else BitmapDescriptorFactory.defaultMarker(
                            BitmapDescriptorFactory.HUE_BLUE
                        )
                    )
                    .title(
                        if (item.type == PeopleType.ENDANGERED.name.toLowerCase(Locale.getDefault()))
                            resourceProvider.getEndangeredPerson() else resourceProvider.getVolunteer()
                    )
                    .snippet(resourceProvider.getMoreDetails())
                    .draggable(true)] =
                    MarkerDetails(item.id, PeopleType.valueOf(item.type.toUpperCase()))
            }
        }
        return hashMap
    }

    private fun checkMapMarkerColor(lastVisit: String?): BitmapDescriptor? {
        if (lastVisit != null) {
            val localDateTime = DateTimeUtil.convertUtcToLocal(lastVisit)
            val localDateTimeNow = LocalDateTime.now()

            when {
                ChronoUnit.DAYS.between(localDateTime, localDateTimeNow) == 0L -> {
                    return BitmapDescriptorFactory.defaultMarker(
                        BitmapDescriptorFactory.HUE_GREEN
                    )
                }
                ChronoUnit.DAYS.between(localDateTime, localDateTimeNow) >= 7L -> {
                    return BitmapDescriptorFactory.defaultMarker(
                        BitmapDescriptorFactory.HUE_RED
                    )
                }
                ChronoUnit.DAYS.between(localDateTime, localDateTimeNow) in 1..6L -> {
                    return BitmapDescriptorFactory.defaultMarker(
                        BitmapDescriptorFactory.HUE_YELLOW
                    )
                }
                else -> return BitmapDescriptorFactory.defaultMarker(
                    BitmapDescriptorFactory.HUE_GREEN
                )
            }
        } else {
            return BitmapDescriptorFactory.defaultMarker(
                BitmapDescriptorFactory.HUE_GREEN
            )
        }
    }
}

data class MarkerDetails(
    val id: Int,
    val markerType: PeopleType
)