package com.fightcorona

import android.app.Application
import com.fightcorona.di.AppInjector
import com.google.fightcorona.BuildConfig
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasAndroidInjector
import io.sentry.Sentry
import io.sentry.android.AndroidSentryClientFactory
import timber.log.Timber
import javax.inject.Inject

class CoronaApplication : Application(), HasAndroidInjector {

    @Inject
    lateinit var dispatchingAndroidInjector: DispatchingAndroidInjector<Any>

    override fun onCreate() {
        super.onCreate()
        AppInjector.init(this)
        Sentry.init(
            "https://65d217755bd74bc9aa7181f6709e4fd4@sentry.io/5174833",
            AndroidSentryClientFactory(this)
        )
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }

    override fun androidInjector(): AndroidInjector<Any> {
        return dispatchingAndroidInjector
    }
}