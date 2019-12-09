package nl.otters.elbho.models

import com.google.gson.annotations.SerializedName

object Vehicle {

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
    )

    data class Reservation(

        val vehicleId: String,
        val advisorId: String,
        val requestId: String,
        val startDateTime: String,
        val endDateTime: String
    )
}