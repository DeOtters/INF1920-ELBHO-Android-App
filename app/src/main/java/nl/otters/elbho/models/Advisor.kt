package nl.otters.elbho.models

import com.google.gson.annotations.SerializedName

//TODO: remove unnessecary fields
data class Advisor (
    val firstname: String,
    val lastname: String,
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

data class Authentication (
    @SerializedName("Jwt")
    val authToken: String,
    val error: String,
    val message: String
)

data class LoginViewModel (
    val email: String,
    val password: String
)

