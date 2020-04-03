package com.covidvolonter.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.covidvolonter.main.view_models.*
import com.covidvolonter.signin.SigninViewModel
import com.covidvolonter.util.ViewModelFactory
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
    @ViewModelKey(PoiDetailViewModel::class)
    abstract fun personDetailViewModel(poiDetailViewModel: PoiDetailViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(FeedbacksViewModel::class)
    abstract fun feedbacksViewModel(feedbacksViewModel: FeedbacksViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(CreateVisitViewModel::class)
    abstract fun createVisitViewModel(createVisitViewModel: CreateVisitViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(SigninViewModel::class)
    abstract fun signinViewModel(signinViewModel: SigninViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(SettingsViewModel::class)
    abstract fun settingsViewModel(settingsViewModel: SettingsViewModel): ViewModel
}