package com.example.airlinesapp.di.network

import com.example.airlinesapp.models.AirLine
import com.example.airlinesapp.models.Passenger
import com.example.airlinesapp.models.PassengerPost
import com.example.airlinesapp.models.PassengersResponse
import com.example.airlinesapp.util.Constants.PASSENGERS_PER_PAGE
import io.reactivex.Single
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface ApiService {

    @GET("airlines")
    fun getAirlines(): Single<List<AirLine>>

    @GET("passenger")
    fun getPassengers(
        @Query("page") page: Int,
        @Query("size") size: Int = PASSENGERS_PER_PAGE
    ): Single<PassengersResponse>

    @POST("airlines")
    fun addNewAirline(@Body airlineData: AirLine): Single<AirLine>

    @POST("passenger")
    fun addNewPassenger(@Body passengerData: PassengerPost): Single<Passenger>
}