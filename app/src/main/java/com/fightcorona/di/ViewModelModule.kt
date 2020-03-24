package com.fightcorona.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.fightcorona.main.view_models.AddVolunteerViewModel
import com.fightcorona.util.ViewModelFactory
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class ViewModelModule {

    @Binds
    abstract fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(AddVolunteerViewModel::class)
    abstract fun addVolunteerViewModel(addVolunteerViewModel: AddVolunteerViewModel): ViewModel
}