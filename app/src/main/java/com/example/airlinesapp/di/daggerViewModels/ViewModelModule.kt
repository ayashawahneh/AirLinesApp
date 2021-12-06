package com.example.airlinesapp.di.daggerViewModels

import androidx.lifecycle.ViewModel
import com.example.airlinesapp.ui.home.airlines.AirlinesViewModel
import com.example.airlinesapp.ui.home.airlines.addAirline.AddAirlineViewModel
import com.example.airlinesapp.ui.home.passengers.PassengersViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class ViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(AirlinesViewModel::class)
    internal abstract fun bindAirlinesViewModel(viewModel: AirlinesViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(PassengersViewModel::class)
    internal abstract fun bindPassengersViewModel(viewModel: PassengersViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(AddAirlineViewModel::class)
    internal abstract fun bindAddAirlineViewModel(viewModel: AddAirlineViewModel): ViewModel
}