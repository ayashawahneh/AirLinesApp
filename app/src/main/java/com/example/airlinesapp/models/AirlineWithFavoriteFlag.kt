package com.example.airlinesapp.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class AirlineWithFavoriteFlag(
    val airline : AirLine,
    var isFavorite: Boolean = false
) : Parcelable {
    override fun toString(): String {
        return airline.name
    }
}