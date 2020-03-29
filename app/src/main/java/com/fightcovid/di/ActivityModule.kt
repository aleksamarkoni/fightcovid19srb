package com.fightcovid.di

import com.fightcovid.main.MainActivity
import com.fightcovid.signin.SignInActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ActivityModule {

    @ContributesAndroidInjector(modules = [FragmentBuildersModule::class])
    internal abstract fun contributeMainActivity(): MainActivity

    @ContributesAndroidInjector(modules = [FragmentBuildersModule::class])
    internal abstract fun contributeSigninActivity(): SignInActivity
}