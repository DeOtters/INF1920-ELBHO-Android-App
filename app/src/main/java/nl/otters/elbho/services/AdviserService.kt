package nl.otters.elbho.services

import nl.otters.elbho.models.Adviser
import retrofit2.Call
import retrofit2.http.*

interface AdviserService {

    @Headers("Content-type: application/json")
    @POST("advisor/login")
    fun login(@Body loginCredentials: Adviser.Login): Call<Adviser.Authentication>

    @Headers("Content-type: application/json")
    @GET("advisors/me")
    fun getAdviser(@Header("x-jwt") auth: String): Call<Adviser.Properties>
}