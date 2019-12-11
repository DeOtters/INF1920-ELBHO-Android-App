package nl.otters.elbho.models

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

object Availability {
    @Parcelize
    data class Slot (
        val id: String,
        @SerializedName("advisorGuid")
        val adviserId: String,
        @SerializedName("startdatetime")
        val startDateTime: String,
        @SerializedName("enddatetime")
        val endDateTime: String,
        val createdDate: String,
        val modifiedDate: String
    ): Parcelable
}