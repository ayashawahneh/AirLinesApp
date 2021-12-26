package com.example.airlinesapp.models

import com.google.gson.annotations.SerializedName
import java.math.BigDecimal

data class AirLine(
    val country: String,
    val established: String,
    @SerializedName("head_quaters")
    val headQuaters: String,
    val id: BigDecimal,
    val logo: String,
    val name: String,
    val slogan: String,
    val website: String
)