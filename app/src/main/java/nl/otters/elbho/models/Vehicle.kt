package nl.otters.elbho.models

import com.google.gson.annotations.SerializedName

object Vehicle {

    data class Car(

        val id: String,
        @SerializedName("licenseplate")
        val licensePlate: String,
        val brand: String,
        val type: String,
        val transmission: String,
        val color: String,
        val location: String,
        val picture: String
    )

    data class Reservation(

        @SerializedName("vehicle-id")
        val vehicleId: Int,
        @SerializedName("user-id")
        val userId: Int,
        @SerializedName("request-id")
        val requestId: Int,
        @SerializedName("datetimeslot")
        val dateTimeSlot: DateTimeSlot
    )

    data class DateTimeSlot(

        @SerializedName("start-datetime")
        val startDateTime: String,
        @SerializedName("end-datetime")
        val endDateTime: String
    )
}