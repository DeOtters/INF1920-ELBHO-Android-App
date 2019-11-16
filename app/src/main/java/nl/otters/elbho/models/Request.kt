package nl.otters.elbho.models

import com.google.gson.annotations.SerializedName

object Request {

    data class Assignment(

        val id: String,
        @SerializedName("company-id")
        val companyId: String,
        @SerializedName("advisor-id")
        val adviserId: String,
        @SerializedName("appointment-datetime")
        val appointmentDateTime: String,
        val comment: String,
        val address: String,
        @SerializedName("phonenumber")
        val phoneNumber: String,
        @SerializedName("contactperson-name")
        val contactName: String,
        @SerializedName("contactperson-phonenumber")
        val contactPhone: String,
        @SerializedName("contactperson-function")
        val contactFunction: String,
        val active: Boolean,
        val website: String,
        val logo: String,
        @SerializedName("kvk-number")
        val kvkNumber: Int,
        @SerializedName("kvk-name")
        val kvkName: String,
        @SerializedName("first-choice")
        val firstChoice: String,
        @SerializedName("second-choice")
        val secondChoice: String,
        @SerializedName("third-choice")
        val thirdChoice: String
    )

    data class Accept(

        @SerializedName("advisor-id")
        val adviserId: String,
        @SerializedName("request-id")
        val requestId: String
    )
}