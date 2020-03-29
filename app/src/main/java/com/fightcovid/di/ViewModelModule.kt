package com.fightcovid.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.fightcovid.main.view_models.*
import com.fightcovid.util.ViewModelFactory
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class ViewModelModule {

    @Binds
    abstract fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(AddPersonViewModel::class)
    abstract fun addVolunteerViewModel(addPersonViewModel: AddPersonViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(MapViewModel::class)
    abstract fun mapViewModel(mapViewModel: MapViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(PersonDetailViewModel::class)
    abstract fun personDetailViewModel(personDetailViewModel: PersonDetailViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(FeedbacksViewModel::class)
    abstract fun feedbacksViewModel(feedbacksViewModel: FeedbacksViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(CreateVisitViewModel::class)
    abstract fun createVisitViewModel(createVisitViewModel: CreateVisitViewModel): ViewModel
}