package nl.otters.elbho.models

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

object Vehicle {

    @Parcelize
    data class Car(

        @SerializedName("_id")
        val id: String,
        val licensePlate: String,
        val brand: String,
        val model: String,
        val transmission: String,
        val location: String,
        val image: String,
        val createdAt: String,
        val updatedAt: String
    ) : Parcelable

    @Parcelize
    data class Reservation(

        @SerializedName("_id")
        val id: String,
        val vehicle: Car,
        @SerializedName("advisor")
        val adviser: String,
        val date: String,
        val start: String,
        val end: String,
        val createdAt: String,
        val updatedAt: String
    ) : Parcelable

    data class CreateReservation(

        val advisor: String,
        val vehicle: String,
        val date: String,
        val start: String,
        val end: String
    )

    data class CarOptions(

        val page: Int?,
        val limit: Int?
    )

    data class ReservationOptions(

        val after: String?,
        val sort: String?
    )
}