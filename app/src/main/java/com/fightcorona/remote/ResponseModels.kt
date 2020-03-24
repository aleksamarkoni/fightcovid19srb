package com.fightcorona.remote

import com.google.gson.annotations.SerializedName

data class Poi(
    @SerializedName("") val address: String,
    @SerializedName("lat") val lat: Float,
    @SerializedName("lon") val lon: Float
)