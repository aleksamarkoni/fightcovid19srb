package com.fightcovid.remote.repository

import com.fightcovid.remote.FightCorona19RestService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class UserRepository(private val restService: FightCorona19RestService) {

    suspend fun getUser() =
        withContext(Dispatchers.IO) {
            val response = restService.getUser()
            return@withContext response.isSuccessful
        }
}