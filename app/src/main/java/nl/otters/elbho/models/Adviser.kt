package nl.otters.elbho.models

import com.google.gson.annotations.SerializedName


object Adviser {

    data class Properties(

        val id: String,
        @SerializedName("firstname")
        val firstName: String,
        @SerializedName("lastname")
        val lastName: String,
        val email: String,
        val password: String,
        val gender: String,
        @SerializedName("phonenumber")
        val phoneNumber: String,
        val active: String,
        val status: String,
        val location: String,
        @SerializedName("workarea")
        val workArea: String,
        val region: String
    )

    //TODO: Change to coordinates
    data class Location(

        val location: String
    )

    data class Status(

        val status: String
    )

    data class Authentication(

        @SerializedName("jwt")
        val authToken: String,
        @SerializedName("advisor-id")
        val adviserId: Int
    )

    data class ErrorMessage(

        val message: String
    )

    data class Login(

        val email: String,
        val password: String
    )
}