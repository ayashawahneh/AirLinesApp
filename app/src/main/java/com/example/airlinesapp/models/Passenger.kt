package com.example.airlinesapp.models

import java.math.BigInteger

data class Passenger(
    val _id: String,
    val name: String,
    val trips: BigInteger,
    val airline: List<AirLine>,
    val __v: String
)