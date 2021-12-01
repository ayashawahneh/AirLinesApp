package com.example.airlinesapp.context

import android.app.Application
import com.example.airlinesapp.di.AppComponent
import com.example.airlinesapp.di.DaggerAppComponent


open class MyApplication : Application() {

    val appComponent: AppComponent by lazy {
        DaggerAppComponent.factory().create(applicationContext)
    }
}