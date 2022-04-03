package com.example.airlinesapp.di

import android.content.Context
import com.example.airlinesapp.context.MyApplication
import com.example.airlinesapp.di.daggerViewModels.ViewModelFactoryModule
import com.example.airlinesapp.di.daggerViewModels.ViewModelModule
import com.example.airlinesapp.di.modules.AppModule
import com.example.airlinesapp.di.modules.BuildersModule
import com.example.airlinesapp.di.modules.RetrofitModule
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjector
import dagger.android.support.AndroidSupportInjectionModule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import javax.inject.Singleton

@ExperimentalCoroutinesApi
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
    override fun inject(application: MyApplication)

    @Component.Builder
    interface Builder {
        @BindsInstance
        fun application(myApplication: MyApplication): Builder

        @BindsInstance
        fun context(context: Context): Builder

        fun build(): AppComponent
    }
}
