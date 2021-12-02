package com.example.airlinesapp.di.network

import com.example.airlinesapp.models.AirLine
import com.example.airlinesapp.models.PassengersResponse
import com.example.airlinesapp.util.Constants.PASSENGERS_PER_PAGE
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query


interface ApiService {

    @GET("airlines")
    fun getAirlines(): Single<List<AirLine>>

    @GET("passenger")
    fun getPassengers(
        @Query("page") page: Int,
        @Query("size") size: Int = PASSENGERS_PER_PAGE
    ): Single<PassengersResponse>
}