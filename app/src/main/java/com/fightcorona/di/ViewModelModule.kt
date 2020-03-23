package com.fightcorona.di

import androidx.lifecycle.ViewModelProvider
import com.fightcorona.util.ViewModelFactory
import dagger.Binds
import dagger.Module

@Module
abstract class ViewModelModule {

    @Binds
    abstract fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory
}