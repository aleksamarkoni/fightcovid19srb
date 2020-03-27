package com.fightcorona.remote

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface FightCorona19RestService {

    @POST("poi")
    suspend fun createPointOfInterest(
        @Body poi: Poi
    ): Response<Void>

    @GET("poi/{latitude}/{longitude}")
    suspend fun getPoi(
        @Path("latitude") lat: Float,
        @Path("longitude") lon: Float
    ): Response<List<MapMarker>>

    @GET("poi/{id}")
    suspend fun getPoiDetail(
        @Path("id") id: Int
    ): Response<PoiDetail>
}