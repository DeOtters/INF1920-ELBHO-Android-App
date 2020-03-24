package nl.otters.elbho.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

object Location {
    @Parcelize
    data class Properties(
        val lon: String,
        val lat: String
    ) : Parcelable
}