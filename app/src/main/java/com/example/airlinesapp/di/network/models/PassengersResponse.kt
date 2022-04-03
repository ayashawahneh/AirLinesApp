package com.example.airlinesapp.di.network.models

import com.google.gson.annotations.SerializedName
import java.math.BigInteger


data class PassengersResponse(
    @SerializedName("data")
    val data: List<Passenger>,
    @SerializedName("totalPages")
    val totalPages: Int,
    @SerializedName("totalPassengers")
    val totalPassengers: BigInteger
)