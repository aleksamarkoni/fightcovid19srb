package com.fightcovid.util

import android.content.Intent
import kotlinx.coroutines.flow.Flow

interface AccountService {
    suspend fun checkForTokens(): AccountResult
    suspend fun logout(): Flow<LogoutResults>
    suspend fun login(intent: Intent?): Flow<AccountResult>
}

sealed class AccountResult
object LoginSuccess : AccountResult()
object FirebaseLoginSuccess : AccountResult()
object TokenSaved : AccountResult()
data class UserLoggedIn(val isLoggedIn: Boolean) : AccountResult()
data class AccountError(val error: String?) : AccountResult()

sealed class LogoutResults
object LogoutResult : LogoutResults()
data class LogoutError(val error: String?) : LogoutResults()