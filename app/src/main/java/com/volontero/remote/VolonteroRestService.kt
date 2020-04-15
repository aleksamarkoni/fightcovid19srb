package com.volontero.remote

import retrofit2.Response
import retrofit2.http.*

interface VolonteroRestService {

    @POST("poi")
    suspend fun createPointOfInterest(
        @Body poi: Poi
    ): Response<EmptyResponse>

    @POST("note")
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

    @GET("note")
    suspend fun getNotesForPoi(@Query("poi_id") poiId: Int): Response<List<Note>>
}