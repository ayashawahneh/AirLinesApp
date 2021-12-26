package com.example.airlinesapp.models

import java.math.BigInteger


data class PassengersResponse(
    val data: List<Passenger>,
    val totalPages: Int,
    val totalPassengers: BigInteger
)