package com.covidvolonter.di

import android.app.Application
import com.covidvolonter.remote.FightCorona19RestService
import com.covidvolonter.remote.GoogleAuthTokenInterceptor
import com.covidvolonter.remote.RetrofitUtils
import com.covidvolonter.remote.repository.PoiRepository
import com.covidvolonter.remote.repository.UserRepository
import com.covidvolonter.util.AccountService
import com.covidvolonter.util.GoogleSigninService
import com.covidvolonter.util.TinyDb
import com.covidvolonter.util.resource_proivder.AndroidResourceProvider
import com.covidvolonter.util.resource_proivder.ResourceProvider
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
        tinyDb: TinyDb,
        resourceProvider: ResourceProvider
    ): PoiRepository =
        PoiRepository(
            restService,
            retrofitUtils,
            tinyDb,
            resourceProvider
        )

    @Singleton
    @Provides
    internal fun provideUserRepository(
        restService: FightCorona19RestService,
        accountService: AccountService,
        retrofitUtils: RetrofitUtils
    ): UserRepository = UserRepository(restService, accountService, retrofitUtils)

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
    fun provideGoogleAuthTokenInterceptor(tinyDb: TinyDb) =
        GoogleAuthTokenInterceptor(tinyDb)

    @Provides
    @Singleton
    fun provideTinyDb(context: Application) = TinyDb(context)

    @Provides
    @Singleton
    fun provideGoogleSigninService(
        firebaseAuth: FirebaseAuth,
        tinyDb: TinyDb,
        context: Application
    ): AccountService = GoogleSigninService(firebaseAuth, tinyDb, context)

    @Provides
    @Singleton
    fun provideResourceProvider(context: Application): ResourceProvider =
        AndroidResourceProvider(context)
}