package com.fightcovid.remote

import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import okhttp3.Interceptor
import okhttp3.Response
import timber.log.Timber

class GoogleAuthTokenInterceptor(private val firebaseAuth: FirebaseAuth) : Interceptor {

    @Throws(NoTokenException::class, ApiException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val task = firebaseAuth.currentUser?.getIdToken(false)
        val token: String
        try {
            val tokenResult = task?.getResult(ApiException::class.java)
            token = tokenResult?.token!!
        } catch (e: ApiException) {
            Timber.e(e)
            throw NoTokenException()
        }
        return chain.run {
            proceed(
                request()
                    .newBuilder()
                    .addHeader(
                        "Authorisation",
                        token
                    )
                    .build()
            )
        }
    }
}