package com.example.airlinesapp.di.daggerViewModels

import androidx.lifecycle.ViewModel
import com.example.airlinesapp.ui.home.airlines.AirlinesViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class ViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(AirlinesViewModel::class)
    internal abstract fun bindAirlinesViewModel(viewModel: AirlinesViewModel): ViewModel
}