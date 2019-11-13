package nl.otters.elbho.services

import nl.otters.elbho.models.Adviser
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface AdviserService {

    @Headers("Content-type: application/json")
    @POST("advisor/login")
    fun login(@Body loginCredentials: Adviser.Login): Call<Adviser.Authentication>
}