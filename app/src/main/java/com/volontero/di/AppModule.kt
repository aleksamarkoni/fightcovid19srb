package com.volontero.di

import android.app.Application
import com.volontero.remote.VolonteroRestService
import com.volontero.remote.GoogleAuthTokenInterceptor
import com.volontero.remote.RetrofitUtils
import com.volontero.remote.repository.PoiRepository
import com.volontero.remote.repository.UserRepository
import com.volontero.util.AccountService
import com.volontero.util.GoogleSigninService
import com.volontero.util.TinyDb
import com.volontero.util.resource_proivder.AndroidResourceProvider
import com.volontero.util.resource_proivder.ResourceProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.volontero.BuildConfig
import com.volontero.BuildConfig.BASE_URL
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
        restService: VolonteroRestService,
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
        restService: VolonteroRestService,
        accountService: AccountService,
        retrofitUtils: RetrofitUtils
    ): UserRepository = UserRepository(restService, accountService, retrofitUtils)

    @Singleton
    @Provides
    internal fun provideRestService(retrofit: Retrofit): VolonteroRestService {
        return retrofit.create(VolonteroRestService::class.java)
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