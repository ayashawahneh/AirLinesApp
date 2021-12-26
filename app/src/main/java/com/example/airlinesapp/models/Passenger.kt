package com.example.airlinesapp.models

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize
import java.math.BigInteger

@Parcelize
data class Passenger(
    @SerializedName("_id")
    val id: String?,
    @SerializedName("name")
    val name: String?="",
    @SerializedName("trips")
    val trips: BigInteger?,
    @SerializedName("airline")
    val airline: List<AirLine>?,
    @SerializedName("__v")
    val v: Int?
) : Parcelable