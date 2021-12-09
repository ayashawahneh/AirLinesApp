package com.example.airlinesapp.models

import com.google.gson.annotations.SerializedName
import java.math.BigDecimal

data class PassengerPost(
    @SerializedName("name")
    val name: String,
    @SerializedName("trips")
    val trips: Int,
    @SerializedName("airline")
    val airline: BigDecimal
)
