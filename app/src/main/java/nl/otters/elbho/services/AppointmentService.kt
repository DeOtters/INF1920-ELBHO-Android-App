package nl.otters.elbho.services

import nl.otters.elbho.models.Request
import retrofit2.Call
import retrofit2.http.*

interface AppointmentService {

    @Headers("Content-type: application/json")
    @GET("/auth/appointment/me")
    fun getAppointments(@Header("Authorization") auth: String, @Query("page") page: Int?, @Query("limit") limit: Int?, @Query("before") before: String?, @Query("after") after: String?, @Query("sort") sort: String? ): Call<ArrayList<Request.Properties>>
}