package com.fightcorona.remote

class PoiRepository(
    private val fightCorona19Service: FightCorona19RestService
) {
    suspend fun createPointOfInterest(latitude: Float, longitude: Float) {
        val response = fightCorona19Service.createPointOfInterest(latitude, longitude)
    }
}