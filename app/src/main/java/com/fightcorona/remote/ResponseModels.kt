package com.fightcorona.remote

import com.google.gson.annotations.SerializedName

data class MapMarker(
    @SerializedName("id")
    val id: Int,
    @SerializedName("type")
    val type: String,
    @SerializedName("latitude")
    val latitude: Float,
    @SerializedName("longitude")
    val longitude: Float
)

class EmptyResponse() : Exception()