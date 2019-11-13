package nl.otters.elbho.models

import com.google.gson.annotations.SerializedName


object Adviser {
    //TODO: remove unused fields
    data class Properties(

        @SerializedName("firstname")
        val firstName: String,
        @SerializedName("lastname")
        val lastName: String,
        val gender: String,
        val phoneNumber: Int,
        val active: Boolean,
        val status: String,
        val location: String,
        val workArea: String,
        val region: String,
        val permissionLevel: Int,
        val email: String,
        val password: String,
        val id: String,
        val createdDate: String,
        val modifiedDate: String
    )

    //TODO: Change to co-ordinates
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