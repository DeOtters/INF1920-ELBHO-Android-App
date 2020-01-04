package nl.otters.elbho.services

import nl.otters.elbho.models.Adviser
import retrofit2.Call
import retrofit2.http.*

interface AdviserService {

    @Headers("Content-type: application/json")
    @POST("login")
    fun login(@Body loginCredentials: Adviser.Login): Call<Adviser.Authentication>

    @Headers("Content-type: application/json")
    @GET("auth/advisor/me")
    fun getAdviser(@Header("Authorization") auth: String): Call<Adviser.Properties>
}