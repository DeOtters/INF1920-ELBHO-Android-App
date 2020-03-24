package nl.otters.elbho.models

import com.google.gson.annotations.SerializedName

object Adviser {

    data class Properties(

        @SerializedName("_id")
        val id: String,
        val firstName: String,
        val lastName: String,
        val gender: String,
        val email: String,
        val phoneNumber: String,
        val active: Boolean,
        val status: String,
        val location: String,
        val workArea: String,
        val region: String,
        val permissionLevel: Int,
        val lastPinged: String,
        val createdAt: String,
        val updatedAt: String
    )

    data class Authentication(
        val token: String
    )

    data class Login(
        val email: String,
        val password: String
    )
}
