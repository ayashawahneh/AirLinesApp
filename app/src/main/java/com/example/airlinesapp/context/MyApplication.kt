package com.example.airlinesapp.context

import android.app.Application
import com.example.airlinesapp.di.AppComponent
import com.example.airlinesapp.di.DaggerAppComponent
import com.example.airlinesapp.di.modules.RetrofitModule
import dagger.android.AndroidInjector
import dagger.android.support.DaggerApplication


//open class MyApplication : Application() {
//
//    private lateinit var appComponent: AppComponent
//
//    override fun onCreate() {
//        super.onCreate()
//
//        appComponent = DaggerAppComponent.builder()
//            .retrofitModule(RetrofitModule())
//            .build()
//    }
//
//    fun getRetroComponent(): AppComponent{
//        return appComponent
//    }
//}

class MyApplication : DaggerApplication() {
    override fun applicationInjector(): AndroidInjector<out DaggerApplication> {
        return DaggerAppComponent.factory().create(this)
    }
}