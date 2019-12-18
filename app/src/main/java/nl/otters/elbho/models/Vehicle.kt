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
        val start: String,
        val end: String,
        val createdAt: String,
        val updatedAt: String
    ) : Parcelable

    data class CreateReservation(

        val vehicle: String,
        val date: String,
        val start: String,
        val end: String
    )
}