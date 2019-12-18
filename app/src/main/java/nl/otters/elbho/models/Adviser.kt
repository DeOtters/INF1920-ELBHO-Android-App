package nl.otters.elbho.models

import com.google.gson.annotations.SerializedName


object Adviser {

    data class Properties(

        @SerializedName("_id")
        val id: String,
        val active: Boolean,
        val location: String,
        val permissionLevel: Int,
        val firstName: String,
        val lastName: String,
        val gender: String,
        val phoneNumber: String,
        val status: String,
        val workArea: String,
        val region: String,
        val email: String,
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
