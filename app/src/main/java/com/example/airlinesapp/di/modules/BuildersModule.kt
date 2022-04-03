package com.example.airlinesapp.di.modules

import com.example.airlinesapp.ui.home.airlines.AirlinesFragment
import com.example.airlinesapp.ui.home.airlines.add.AddAirlineActivity
import com.example.airlinesapp.ui.home.passengers.PassengersFragment
import com.example.airlinesapp.ui.home.passengers.addedit.AddEditPassengerActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
@Module
abstract class BuildersModule {

    @ContributesAndroidInjector
    abstract fun contributeAirlinesFragment(): AirlinesFragment

    @ContributesAndroidInjector
    abstract fun contributePassengersFragment(): PassengersFragment

    @ContributesAndroidInjector
    abstract fun contributeAddAirlineActivity(): AddAirlineActivity

    @ContributesAndroidInjector
    abstract fun contributeEditPassengerActivity(): AddEditPassengerActivity
}