package com.example.airlinesapp.ui.home.airlines

import com.example.airlinesapp.di.network.ApiService
import com.example.airlinesapp.models.AirLine
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AirlinesRepository @Inject constructor(private val apiService: ApiService) {

    fun getAirlines(): Single<List<AirLine>> {
        return apiService.getAirlines().subscribeOn(Schedulers.io())
    }

}