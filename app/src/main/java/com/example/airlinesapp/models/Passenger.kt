package com.example.airlinesapp.models

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize
import kotlinx.android.parcel.RawValue

@Parcelize
data class Passenger(

    @SerializedName("_id")
    val id: String,
    val name: String,
    val trips: Int,
    val airline: List<AirLine>,
   @SerializedName("__v")
    val v: Int
) : Parcelable