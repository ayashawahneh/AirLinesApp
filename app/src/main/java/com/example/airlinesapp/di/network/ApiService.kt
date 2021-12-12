package com.example.airlinesapp.di.network

import com.example.airlinesapp.models.AirLine
import io.reactivex.Single
import retrofit2.http.GET


interface ApiService {

    @GET("airlines")
    fun getAirlines(): Single<List<AirLine>>
}