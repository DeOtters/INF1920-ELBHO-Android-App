package nl.otters.elbho.services

import nl.otters.elbho.models.Authentication
import nl.otters.elbho.models.LoginViewModel
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface AdvisorService {
    @Headers("Content-type: application/json")
    @POST("advisor/login")
    fun login(@Body loginCredentials: LoginViewModel): Call<Authentication>
}