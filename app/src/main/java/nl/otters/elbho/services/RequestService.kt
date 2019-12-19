package nl.otters.elbho.services

import nl.otters.elbho.models.Request
import retrofit2.Call
import retrofit2.http.*

interface RequestService {
    @Headers("Content-type: application/json")
    @GET("auth/request/me")
    fun getAllRequests(@Header("Authorization") auth: String): Call<ArrayList<Request.Properties>>

    @Headers("Content-type: application/json")
    @PUT("auth/request/{appointmentId}")
    fun respondToRequest(@Header("Authorization") auth: String, @Path("appointmentId") accept: Boolean): Call<Unit>
}