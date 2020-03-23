package com.fightcorona.di

import android.app.Application
import com.fightcorona.CoronaApplication
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjectionModule
import dagger.android.support.AndroidSupportInjectionModule
import javax.inject.Singleton

@Singleton
@Component(
    modules = [AndroidInjectionModule::class,
        AndroidSupportInjectionModule::class,
        AppModule::class,
        ActivityModule::class,
        FragmentBuildersModule::class,
        ViewModelModule::class]
)
interface AppComponent {

    fun inject(appController: CoronaApplication)

    @Component.Builder
    interface Builder {

        @BindsInstance
        fun application(application: Application): Builder

        fun build(): AppComponent
    }
}