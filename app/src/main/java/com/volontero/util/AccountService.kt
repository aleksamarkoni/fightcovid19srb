package com.volontero.util

import android.content.Intent
import kotlinx.coroutines.flow.Flow

interface AccountService {
    suspend fun checkForTokens(): Flow<TokenResult>
    suspend fun logout(): Flow<LogoutResults>
    suspend fun login(intent: Intent?): Flow<AccountResult>
}

sealed class AccountResult
object LoginSuccess : AccountResult()
object FirebaseLoginSuccess : AccountResult()
object TokenSaved : AccountResult()
data class AccountError(val error: String?) : AccountResult()

sealed class LogoutResults
object LogoutResult : LogoutResults()
data class LogoutError(val error: String?) : LogoutResults()

sealed class TokenResult
object TokenValid : TokenResult()
object TokenExpired : TokenResult()