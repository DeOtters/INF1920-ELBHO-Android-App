package nl.otters.elbho.models

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

object Appointment {
    @Parcelize
    data class Properties(
        @SerializedName("_id")
        val id: String,
        val startTime: String,
        val endTime: String,
        val comment: String,
        val address: String,
        val contactPersonName: String,
        val contactPersonPhoneNumber: String,
        val contactPersonFunction: String,
        val contactPersonEmail: String,
        val active: String,
        val website: String,
        val logo: String,
        val cocNumber: String,
        val cocName: String,
        @SerializedName("advisor")
        val adviser: String,
        val createdAt: String,
        val updatedAt: String
    ) : Parcelable

    data class Options(
        val page: Int?,
        val limit: Int?,
        val before: String?,
        val after: String?,
        val sort: String?
    )
}
