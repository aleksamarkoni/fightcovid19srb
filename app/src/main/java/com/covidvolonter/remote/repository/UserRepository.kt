package com.covidvolonter.remote.repository

import android.content.Intent
import com.covidvolonter.remote.FightCorona19RestService
import com.covidvolonter.remote.RetrofitUtils
import com.covidvolonter.util.AccountResult
import com.covidvolonter.util.AccountService
import com.covidvolonter.util.LogoutResults
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import timber.log.Timber

class UserRepository(
    private val restService: FightCorona19RestService,
    private val accountService: AccountService,
    private val retrofitUtils: RetrofitUtils
) {

    suspend fun getUser() =
        withContext(Dispatchers.IO) {
            val response = restService.getUser()
            val result = retrofitUtils.handleResponse(response)
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