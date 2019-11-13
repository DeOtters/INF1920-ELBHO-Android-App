package nl.otters.elbho.models

import com.google.gson.annotations.SerializedName

object Availability {

    data class Slot(

        @SerializedName("availability-id")
        val id: String,
        @SerializedName("advisor-id")
        val adviserId: String,
        @SerializedName("startdatetime")
        val startDateTime: String,
        @SerializedName("enddatetime")
        val endDateTime: String
    )
}