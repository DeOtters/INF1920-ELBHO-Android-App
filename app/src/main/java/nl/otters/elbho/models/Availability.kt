package nl.otters.elbho.models

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

object Availability {
    @Parcelize
    data class Slot (
        @SerializedName("_id")
        val id: String,
        @SerializedName("advisor")
        val adviserId: String,
        val date: String,
        val start: String,
        val end: String,
        val createdAt: String,
        val updatedAt: String
    ): Parcelable

    data class TimePeriod (
        val before: String,
        val after: String
    )
}