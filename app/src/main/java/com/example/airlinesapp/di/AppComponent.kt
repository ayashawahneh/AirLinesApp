package com.example.airlinesapp.di

import android.app.Application
import com.example.airlinesapp.context.MyApplication
import com.example.airlinesapp.di.daggerViewModels.ViewModelFactory
import com.example.airlinesapp.di.daggerViewModels.ViewModelFactoryModule
import com.example.airlinesapp.di.daggerViewModels.ViewModelModule
import com.example.airlinesapp.di.modules.AppModule
import com.example.airlinesapp.di.modules.BuildersModule
import com.example.airlinesapp.di.modules.RetrofitModule
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjector
import dagger.android.support.AndroidSupportInjectionModule
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        AndroidSupportInjectionModule::class,
        ViewModelModule::class,
        AppModule::class,
        BuildersModule::class,
        ViewModelFactoryModule::class,
        RetrofitModule::class
    ]
)
interface AppComponent : AndroidInjector<MyApplication> {
    @Component.Factory
    interface Factory {
        fun create(@BindsInstance application: Application): AppComponent
    }
}