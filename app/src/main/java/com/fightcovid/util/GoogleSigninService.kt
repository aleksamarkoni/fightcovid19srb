package com.fightcovid.util

import android.content.Context
import android.content.Intent
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import timber.log.Timber
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class GoogleSigninService(
    private val firebaseAuth: FirebaseAuth,
    private val tinyDb: TinyDb,
    private val context: Context
) : AccountService {

    override suspend fun checkForTokens(): AccountResult =
        suspendCoroutine { continuation ->
            val token = tinyDb.getString(TOKEN)
            continuation.resume(
                if (token.isNullOrBlank()) UserLoggedIn(false) else UserLoggedIn(
                    true
                )
            )
        }

    @ExperimentalCoroutinesApi
    override suspend fun logout(): Flow<LogoutResults> = channelFlow {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).build()
        val signInTask = GoogleSignIn.getClient(context, gso).signOut()
        signInTask.addOnCompleteListener {
            if (it.isComplete) {
                firebaseAuth.signOut()
                tinyDb.clear()
                offer(LogoutResult)
            } else {
                offer(LogoutError("Error signing out"))
            }
        }
        awaitClose {
            Timber.d("Channel close")
        }
    }

    @ExperimentalCoroutinesApi
    override suspend fun login(intent: Intent?): Flow<AccountResult> = channelFlow {
        val task = GoogleSignIn.getSignedInAccountFromIntent(intent)
        try {
            val account = task?.getResult(ApiException::class.java)
            val credential = GoogleAuthProvider.getCredential(account?.idToken, null)
            val taskAuthResult = firebaseAuth.signInWithCredential(credential)

            taskAuthResult.addOnCompleteListener {
                if (it.isSuccessful) {
                    val getTokenResultTask = firebaseAuth.currentUser?.getIdToken(false)
                    getTokenResultTask?.addOnCompleteListener { tokenTask ->
                        if (tokenTask.isSuccessful) {
                            offer(FirebaseLoginSuccess)
                            try {
                                val tokenResult =
                                    getTokenResultTask.getResult(ApiException::class.java)
                                tokenResult?.token?.let { token ->
                                    Timber.d("Stored token is $token")
                                    tinyDb.putString(TOKEN, token)
                                    offer(TokenSaved)
                                }
                            } catch (e: ApiException) {
                                offer(AccountError(e.message))
                            }
                        } else {
                            offer(AccountError("Get token result task fail"))
                        }
                    }
                } else {
                    offer(AccountError(it.exception?.message))
                }
            }
        } catch (e: ApiException) {
            offer(AccountError(e.message))
        }

        awaitClose {
            Timber.d("Closing channel for my flow")
        }
    }
}