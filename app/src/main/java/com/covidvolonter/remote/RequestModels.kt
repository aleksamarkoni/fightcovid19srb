package com.covidvolonter.remote

import com.google.gson.annotations.SerializedName

data class Poi(
    @SerializedName("type") val type: String,
    @SerializedName("latitude") val lat: Float,
    @SerializedName("longitude") val lon: Float,
    @SerializedName("address") val address: String,
    @SerializedName("floor") val floor: Int?,
    @SerializedName("apartment") val apartment: String,
    @SerializedName("phone") val phone: String?,
    @SerializedName("note") val note: String?
)

data class Visit(
    @SerializedName("poi_id") val poiId: Int,
    @SerializedName("text") val feedback: String,
    @SerializedName("is_visit") val isVisit: Boolean
)