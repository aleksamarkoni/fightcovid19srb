package com.fightcovid.remote

import com.fightcovid.util.TOKEN
import com.fightcovid.util.TinyDb
import com.google.android.gms.common.api.ApiException
import okhttp3.Interceptor
import okhttp3.Response

class GoogleAuthTokenInterceptor(private val tinyDb: TinyDb) : Interceptor {

    @Throws(NoTokenException::class, ApiException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val token = tinyDb.getString(TOKEN) ?: throw NoTokenException()
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