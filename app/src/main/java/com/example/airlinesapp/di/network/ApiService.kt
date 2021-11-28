package com.example.airlinesapp.di.network

import com.example.airlinesapp.models.AirLine
import com.example.airlinesapp.models.Passenger
import com.example.airlinesapp.models.PassengerPost
import com.example.airlinesapp.models.PassengersResponse
import io.reactivex.Single
import retrofit2.http.*


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

    @PUT("passenger/{id}")
    fun editPassenger(
        @Path("id") passengerId: String,
        @Body passengerData: PassengerPost
    ): Single<Passenger>

    @DELETE("passenger/{id}")
    fun deletePassenger(
        @Path("id") passengerId: String
    ): Single<Any>

    companion object {

        const val PASSENGERS_PER_PAGE = 10
    }
}