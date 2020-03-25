package com.fightcorona.remote

import retrofit2.Response
import retrofit2.http.POST
import retrofit2.http.Query

interface FightCorona19RestService {

    @POST("poi")
    suspend fun createPointOfInterest(
        @Query("lat") lat: Float,
        @Query("lon") lon: Float
    ): Response<Void>
}