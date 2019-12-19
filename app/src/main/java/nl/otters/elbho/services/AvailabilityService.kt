package nl.otters.elbho.services

import nl.otters.elbho.models.Availability
import retrofit2.Call
import retrofit2.http.*

interface AvailabilityService {
    @Headers("Content-type: application/json")
    @GET("auth/availability/me")
    fun getAvailabilities(@Header("Authorization") auth: String, @Body timePeriod: Availability.TimePeriod? ): Call<ArrayList<Availability.Slot>>

    @Headers("Content-type: application/json")
    @POST("auth/availability")
    fun createAvailability(@Header("Authorization") auth: String, @Body availability: Availability.Slot): Call<Unit>
}