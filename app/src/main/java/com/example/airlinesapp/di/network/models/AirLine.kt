package com.example.airlinesapp.di.network.models

import android.os.Parcelable
import com.example.airlinesapp.models.AirlineWithFavoriteFlagItem
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class AirLine(
    @SerializedName("country")
    val country: String? = null,
    @SerializedName("established")
    val established: String? = null,
    @SerializedName("head_quaters")
    val headQuaters: String,
    @SerializedName("id")
    val id: String? = null,
    @SerializedName("logo")
    val logo: String? = null,
    @SerializedName("name")
    val name: String,
    @SerializedName("slogan")
    val slogan: String? = null,
    @SerializedName("website")
    val website: String? = null
) : Parcelable {
    override fun toString(): String {
        return name
    }
}