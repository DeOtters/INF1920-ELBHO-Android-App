package nl.otters.elbho.models

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

object Request {
    @Parcelize
    data class Properties(val _id: String,
                          val appointment: String,
                          val accepted: Boolean,
                          @SerializedName("currentAdvisorIndex")
                              val currentAdviserIndex: Number,
                          @SerializedName("currentAdvisor")
                              val currentAdviser: String,
                          @SerializedName("advisors")
                              val advisers: ArrayList<String>,
                          val createdAt: String,
                          val updatedAt: String) : Parcelable
}