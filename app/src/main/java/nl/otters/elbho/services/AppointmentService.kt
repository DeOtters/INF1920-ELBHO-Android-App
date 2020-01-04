package nl.otters.elbho.services

import nl.otters.elbho.models.Appointment
import retrofit2.Call
import retrofit2.http.*

interface AppointmentService {

    @Headers("Content-type: application/json")
    @GET("/auth/appointment/me")
    fun getAppointments(@Header("Authorization") auth: String, @Body options: Appointment.Options): Call<ArrayList<Appointment.Properties>>
}