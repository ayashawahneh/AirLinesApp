package com.example.airlinesapp.di.modules

import com.example.airlinesapp.ui.home.airlines.AirlinesFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class BuildersModule {

    @ContributesAndroidInjector
    abstract fun contributeAirlinesFragment(): AirlinesFragment
}