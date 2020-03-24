package com.fightcorona.remote

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface FightCorona19RestService {

    @POST("poi")
    suspend fun createPointOfInterest(@Body poi: Poi): Response<Void>
}