package com.covidvolonter.remote

import com.google.gson.annotations.SerializedName

data class MapMarker(
    @SerializedName("id")
    val id: Int,
    @SerializedName("type")
    val type: String,
    @SerializedName("latitude")
    val latitude: Float,
    @SerializedName("longitude")
    val longitude: Float,
    @SerializedName("last_visit")
    val lastVisit: String?
)

data class PoiDetail(
    @SerializedName("id")
    val id: Int,
    @SerializedName("type")
    val type: String,
    @SerializedName("latitude")
    val latitude: Float,
    @SerializedName("longitude")
    val longitude: Float,
    @SerializedName("address")
    val address: String,
    @SerializedName("apartment")
    val apartment: String,
    @SerializedName("floor")
    val floor: Int,
    @SerializedName("phone")
    val phone: String,
    @SerializedName("note")
    val note: String,
    @SerializedName("creator")
    val creator: PoiCreator,
    @SerializedName("notes")
    val notes: List<Note>
)

data class Note(
    @SerializedName("id")
    val id: Int,
    @SerializedName("text")
    val text: String?,
    @SerializedName("date")
    val date: String,
    @SerializedName("creator")
    val creatorId: PoiCreator,
    @SerializedName("poi_id")
    val poi_id: Int,
    @SerializedName("is_visit")
    val isVisit: Boolean
)

data class PoiCreator(
    @SerializedName("id")
    val id: Int,
    @SerializedName("name")
    val name: String,
    @SerializedName("picture")
    val picture: String?
)

class EmptyResponse() : Exception()