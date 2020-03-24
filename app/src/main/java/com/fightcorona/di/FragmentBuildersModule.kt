package com.fightcorona.di

import com.fightcorona.main.fragments.MapFragment
import com.fightcorona.main.fragments.VolunteerFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class FragmentBuildersModule {

    @ContributesAndroidInjector
    internal abstract fun contributeMapFragment(): MapFragment

    @ContributesAndroidInjector
    internal abstract fun contributeFragmentVolunteer(): VolunteerFragment
}