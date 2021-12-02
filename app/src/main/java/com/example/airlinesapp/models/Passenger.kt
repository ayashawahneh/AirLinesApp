package com.example.airlinesapp.models

import com.google.gson.annotations.SerializedName
import java.math.BigInteger

data class Passenger(
    @SerializedName("_id")
    val id: String,
    val name: String,
    val trips: BigInteger,
    val airline: List<AirLine>,
    @SerializedName("__v")
    val v: String
)