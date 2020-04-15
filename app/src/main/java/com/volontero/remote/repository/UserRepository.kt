package com.volontero.remote.repository

import android.content.Intent
import com.volontero.remote.VolonteroRestService
import com.volontero.remote.RetrofitUtils
import com.volontero.util.AccountResult
import com.volontero.util.AccountService
import com.volontero.util.LogoutResults
import com.volontero.util.TokenResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

class UserRepository(
    private val restService: VolonteroRestService,
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

    suspend fun checkTokens(): Flow<TokenResult> =
        withContext(Dispatchers.IO) {
            return@withContext accountService.checkForTokens()
        }

    suspend fun logout(): Flow<LogoutResults> =
        withContext(Dispatchers.IO) {
            return@withContext accountService.logout()
        }
}