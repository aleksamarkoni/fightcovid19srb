package com.fightcorona.di

import com.fightcorona.main.fragments.MapFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class FragmentBuildersModule {
    @ContributesAndroidInjector
    internal abstract fun contributeMapFragment(): MapFragment

}