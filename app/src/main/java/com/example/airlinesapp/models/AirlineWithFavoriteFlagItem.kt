package com.example.airlinesapp.models

import android.os.Parcelable
import com.example.airlinesapp.di.network.models.AirLine
import kotlinx.android.parcel.Parcelize

@Parcelize
data class AirlineWithFavoriteFlagItem(
    val id: String? = null,
    val name: String,
    val logo: String? = null,
    val country: String? = null,
    var isFavorite: Boolean = false
) : Parcelable {
    override fun toString(): String {
        return name
    }

    companion object {

        fun create(airline: AirLine, isFavorite: Boolean): AirlineWithFavoriteFlagItem =
            AirlineWithFavoriteFlagItem(
                id = airline.id,
                name = airline.name,
                logo = airline.logo,
                country = airline.country,
                isFavorite = isFavorite
            )
    }
}