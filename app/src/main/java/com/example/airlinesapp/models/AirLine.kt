package com.example.airlinesapp.models

import com.google.gson.annotations.SerializedName

data class AirLine(
    val country: String,
    val established: String? = null,
    @SerializedName("head_quaters")
    val headQuaters: String,
    val id: String? = null,
    val logo: String? = null,
    val name: String,
    val slogan: String? = null,
    val website: String? = null
) {
    override fun toString(): String {
        return name
    }
}