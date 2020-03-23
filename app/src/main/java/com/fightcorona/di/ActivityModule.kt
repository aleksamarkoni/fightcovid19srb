package com.fightcorona.di

import com.fightcorona.MainActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ActivityModule {
    @ContributesAndroidInjector(modules = [FragmentBuildersModule::class])
    internal abstract fun contributeMainActivity(): MainActivity
}