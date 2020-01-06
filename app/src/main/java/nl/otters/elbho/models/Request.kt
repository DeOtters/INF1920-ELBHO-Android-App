package nl.otters.elbho.models

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

object Request {
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
        val createdAt: String,
        val updatedAt: String
    ) : Parcelable

    data class Respond (
        val accept: Boolean
    )
}