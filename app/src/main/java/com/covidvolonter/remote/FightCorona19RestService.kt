package com.covidvolonter.remote

import retrofit2.Response
import retrofit2.http.*

interface FightCorona19RestService {

    @POST("poi")
    suspend fun createPointOfInterest(
        @Body poi: Poi
    ): Response<EmptyResponse>

    @POST("visit")
    suspend fun createVisit(
        @Body visit: Visit
    ): Response<EmptyResponse>

    @GET("poi/{latitude}/{longitude}")
    suspend fun getPoi(
        @Path("latitude") lat: Float,
        @Path("longitude") lon: Float,
        @Query("distance") distance: Int?
    ): Response<List<MapMarker>>

    @GET("poi/{id}")
    suspend fun getPoiDetail(
        @Path("id") id: Int
    ): Response<PoiDetail>

    @GET("user")
    suspend fun getUser(): Response<EmptyResponse>
}