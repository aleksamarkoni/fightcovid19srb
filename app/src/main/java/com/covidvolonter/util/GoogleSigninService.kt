package com.covidvolonter.util

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

class GoogleSigninService(
    private val firebaseAuth: FirebaseAuth,
    private val tinyDb: TinyDb,
    private val context: Context
) : AccountService {

    override suspend fun checkForTokens(): AccountResult {
        val token = tinyDb.getString(TOKEN)
        return if (token.isNullOrBlank()) UserLoggedIn(false)
        else UserLoggedIn(true)
    }

    @ExperimentalCoroutinesApi
    override suspend fun logout(): Flow<LogoutResults> = channelFlow {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).build()
        val signInTask = GoogleSignIn.getClient(context, gso).signOut()
        signInTask.addOnCompleteListener {
            if (it.isSuccessful) {
                firebaseAuth.signOut()
                tinyDb.clear()
                offer(LogoutResult)
                close()
            } else {
                offer(LogoutError("Error signing out"))
                close()
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
                    firebaseAuth.currentUser?.let { firebaseUser ->
                        val getTokenResultTask = firebaseUser.getIdToken(false)
                        getTokenResultTask.addOnCompleteListener { tokenTask ->
                            if (tokenTask.isSuccessful) {
                                offer(FirebaseLoginSuccess)
                                try {
                                    val tokenResult =
                                        getTokenResultTask.getResult(ApiException::class.java)
                                    if (tokenResult != null) {
                                        tokenResult.token?.let { token ->
                                            Timber.d("Stored token is $token")
                                            tinyDb.putString(TOKEN, token)
                                            offer(TokenSaved)
                                            close()
                                        }
                                    } else {
                                        offer(AccountError("Token result is null"))
                                    }
                                } catch (e: ApiException) {
                                    offer(AccountError(e.message))
                                    close()
                                }
                            } else {
                                offer(AccountError("Get token result task fail"))
                                close()
                            }
                        }
                    } ?: run {
                        offer(AccountError("Firebase user is null"))
                    }
                } else {
                    offer(AccountError(it.exception?.message))
                    close()
                }
            }
        } catch (e: ApiException) {
            offer(AccountError(e.message))
            close()
        }
        awaitClose {
            Timber.d("Closing channel for my flow")
        }
    }
}