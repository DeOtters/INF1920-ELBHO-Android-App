package nl.otters.elbho.models

import com.google.gson.annotations.SerializedName

object Availability {

    data class Slot(

        val id: String,
        @SerializedName("advisorGuid")
        val adviserId: String,
        @SerializedName("startdatetime")
        val startDateTime: String,
        @SerializedName("enddatetime")
        val endDateTime: String,
        val createdDate: String,
        val modifiedDate: String
    )
}