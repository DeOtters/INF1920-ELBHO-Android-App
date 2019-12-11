package nl.otters.elbho.models

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

object Vehicle {

    @Parcelize
    data class Car(

        val id: String,
        @SerializedName("licenseplate")
        val licensePlate: String,
        val brand: String,
        val model: String,
        val transmission: Boolean,
        val color: String,
        val location: String,
        val picture: String
    ): Parcelable

    @Parcelize
    data class Reservation(

        val id: String,
        val vehicleId: String,
        val advisorId: String,
        val requestId: String,
        val startDateTime: String,
        val endDateTime: String
    ): Parcelable

    data class CreateReservation(

        val vehicleId: String,
        val advisorId: String?,
        val startDateTime: String,
        val endDateTime: String
    )

    @Parcelize
    data class Claim(

        val reservation: Reservation,
        val car: Car
    ): Parcelable
}