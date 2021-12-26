package com.example.airlinesapp.di.modules

import com.example.airlinesapp.ui.home.airlines.AirlinesFragment
import com.example.airlinesapp.ui.home.airlines.addAirline.AddAirlineActivity
import com.example.airlinesapp.ui.home.passengers.PassengersFragment
import com.example.airlinesapp.ui.home.passengers.addPassenger.AddPassengerActivity
import com.example.airlinesapp.ui.home.passengers.editPassenger.EditPassengerActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class BuildersModule {

    @ContributesAndroidInjector
    abstract fun contributeAirlinesFragment(): AirlinesFragment

    @ContributesAndroidInjector
    abstract fun contributePassengersFragment(): PassengersFragment

    @ContributesAndroidInjector
    abstract fun contributeAddAirlineActivity(): AddAirlineActivity

    @ContributesAndroidInjector
    abstract fun contributeAddPassengerActivity(): AddPassengerActivity

    @ContributesAndroidInjector
    abstract fun contributeEditPassengerActivity(): EditPassengerActivity
}