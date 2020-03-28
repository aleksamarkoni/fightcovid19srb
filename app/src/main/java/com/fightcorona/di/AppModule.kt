package com.fightcorona.di

import android.app.Application
import com.fightcorona.remote.FightCorona19RestService
import com.fightcorona.remote.GoogleAuthTokenInterceptor
import com.fightcorona.remote.PoiRepository
import com.fightcorona.remote.RetrofitUtils
import com.fightcorona.util.TinyDb
import com.google.fightcorona.BuildConfig
import com.google.fightcorona.BuildConfig.BASE_URL
import com.google.firebase.auth.FirebaseAuth
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
class AppModule {

    @Singleton
    @Provides
    internal fun providePoiRepository(
        restService: FightCorona19RestService,
        retrofitUtils: RetrofitUtils,
        tinyDb: TinyDb
    ): PoiRepository =
        PoiRepository(restService, retrofitUtils, tinyDb)

    @Singleton
    @Provides
    internal fun provideRestService(retrofit: Retrofit): FightCorona19RestService {
        return retrofit.create(FightCorona19RestService::class.java)
    }

    @Singleton
    @Provides
    internal fun provideRetrofit(gson: Gson, client: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .client(client)
            .build()
    }

    @Singleton
    @Provides
    internal fun provideGson(): Gson {
        return GsonBuilder()
            .create()
    }

    @Singleton
    @Provides
    internal fun providesOkHttpClient(googleAuthTokenInterceptor: GoogleAuthTokenInterceptor): OkHttpClient {
        val httpClientBuilder = OkHttpClient.Builder()
            .addInterceptor(object : Interceptor {
                override fun intercept(chain: Interceptor.Chain): okhttp3.Response {
                    val requestBuilder = chain.request().newBuilder()
                    requestBuilder.header("Content-Type", "application/json")
                    requestBuilder.header("Accept", "application/json")
                    return chain.proceed(requestBuilder.build())
                }
            })
            .addInterceptor(googleAuthTokenInterceptor)
        if (BuildConfig.DEBUG) {
            val loggingInterceptor = HttpLoggingInterceptor()
            loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
            httpClientBuilder.addInterceptor(loggingInterceptor)
        }
        httpClientBuilder.readTimeout(30, TimeUnit.SECONDS)
            .connectTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
        return httpClientBuilder.build()
    }

    @Provides
    @Singleton
    fun provideRetrofitUtils(gson: Gson): RetrofitUtils {
        return RetrofitUtils(gson)
    }

    @Provides
    @Singleton
    fun provideFirebaseAuth(): FirebaseAuth = FirebaseAuth.getInstance()

    @Provides
    @Singleton
    fun provideGoogleAuthTokenInterceptor(firebaseAuth: FirebaseAuth) =
        GoogleAuthTokenInterceptor(FirebaseAuth.getInstance())

    @Provides
    @Singleton
    fun provideTinyDb(context: Application) = TinyDb(context)

}