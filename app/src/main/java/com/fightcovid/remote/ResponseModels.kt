package com.fightcovid.remote

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
    val creator: PoiCreator
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