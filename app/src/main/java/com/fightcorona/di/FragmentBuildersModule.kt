package com.fightcorona.di

import com.fightcorona.main.fragments.*
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class FragmentBuildersModule {

    @ContributesAndroidInjector
    internal abstract fun contributeMapFragment(): MapFragment

    @ContributesAndroidInjector
    internal abstract fun contributeFragmentVolunteer(): AddNewPersonFragment

    @ContributesAndroidInjector
    internal abstract fun contributeChooseAddressFragment(): ChooseAddressFragment

    @ContributesAndroidInjector
    internal abstract fun contributeEndangeredDetailFragment(): EndangeredDetailFragment

    @ContributesAndroidInjector
    internal abstract fun contributeAllFeedbacksFragment(): AllFeedbacksFragment
}