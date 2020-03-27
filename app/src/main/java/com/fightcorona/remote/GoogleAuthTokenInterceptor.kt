package com.fightcorona.remote

import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response

class GoogleAuthTokenInterceptor(private val firebaseAuth: FirebaseAuth) : Interceptor {

    @Throws(NoTokenException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        runBlocking {
            if (firebaseAuth.currentUser == null) {
                throw NoTokenException()
            }
        }
        val builder = chain.request().newBuilder()
        return chain.proceed(builder.build())
    }
}