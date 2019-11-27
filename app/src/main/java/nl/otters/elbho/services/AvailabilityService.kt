package nl.otters.elbho.services

import nl.otters.elbho.models.Availability
import retrofit2.Call
import retrofit2.http.*

interface AvailabilityService {
    @Headers("Content-type: application/json")
    @GET("advisor/me/availabilities")
    fun getAllAvailabilities(@Header("x-jwt") auth: String): Call<ArrayList<Availability>>

    @Headers("Content-type: application/json")
    @GET("/availabilities/{id}")
    fun getAvailability(@Header("x-jwt") auth: String, @Path("id") id: Int): Call<Availability>

    @Headers("Content-type: application/json")
    @POST("/availabilities")
    fun createAvailability(@Header("x-jwt") auth: String, @Body availability: Availability): Call<Unit>

    @Headers("Content-type: application/json")
    @PUT("/availabilities/{id}")
    fun updateAvailability(@Header("x-jwt") auth: String, @Path("id") id: Int, @Body availability: Availability): Call<Unit>

    @Headers("Content-type: application/json")
    @PUT("/availabilities/{id}")
    fun deleteAvailability(@Header("x-jwt") auth: String, @Path("id") id: Int): Call<Unit>
}