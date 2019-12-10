package nl.otters.elbho.models

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

object Request {
    @Parcelize
    data class Properties(val id: String,
                          val appointmentDatetime: String,
                          val comment: String,
                          val address: String,
                          val phoneNumber: String,
                          val contactPersonName: String,
                          val contactPersonPhoneNumber: String,
                          val contactPersonFunction: String,
                          val active: Boolean,
                          val website: String,
                          val logo: String,
                          val cocNumber: String,
                          val cocName: String,
                          val firstChoice: String,
                          val secondChoice: String,
                          val thirdChoice: String,
                          val createdDate: String,
                          val modifiedDate: String) : Parcelable

    data class Accept(
        @SerializedName("advisor-id")
        val adviserId: String,
        @SerializedName("request-id")
        val requestId: String
    )
}