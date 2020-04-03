package com.covidvolonter.remote.repository

import android.content.Intent
import com.covidvolonter.remote.FightCorona19RestService
import com.covidvolonter.util.AccountResult
import com.covidvolonter.util.AccountService
import com.covidvolonter.util.LogoutResults
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

class UserRepository(
    private val restService: FightCorona19RestService,
    private val accountService: AccountService
) {

    suspend fun getUser() =
        withContext(Dispatchers.IO) {
            val response = restService.getUser()
            return@withContext response.isSuccessful
        }

    suspend fun loginUser(intent: Intent?): Flow<AccountResult> =
        withContext(Dispatchers.IO) {
            return@withContext accountService.login(intent)
        }

    suspend fun checkTokens(): AccountResult =
        withContext(Dispatchers.IO) {
            return@withContext accountService.checkForTokens()
        }

    suspend fun logout(): Flow<LogoutResults> =
        withContext(Dispatchers.IO) {
            return@withContext accountService.logout()
        }
}