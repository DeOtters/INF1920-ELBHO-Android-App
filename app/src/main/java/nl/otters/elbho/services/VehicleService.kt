package nl.otters.elbho.services

import nl.otters.elbho.models.Vehicle
import retrofit2.Call
import retrofit2.http.*

interface VehicleService {
    // VEHICLES
    @Headers("Content-type: application/json")
    @GET("/auth/vehicle")
    fun getAllVehicles(@Header("Authorization") auth: String, @Body options: Vehicle.CarOptions?): Call<ArrayList<Vehicle.Car>>

    // VEHICLE RESERVATIONS
    @Headers("Content-type: application/json")
    @GET("/auth/reservation")
    fun getAllVehicleReservations(@Header("Authorization") auth: String, @Body options: Vehicle.ReservationOptions?): Call<ArrayList<Vehicle.Reservation>>

    @Headers("Content-type: application/json")
    @GET("/auth/reservation/me")
    fun getAllVehicleReservationsByAdviser(@Header("Authorization") auth: String, @Body after: String): Call<ArrayList<Vehicle.Reservation>>

    @Headers("Content-type: application/json")
    @POST("/auth/reservation")
    fun createVehicleReservation(@Header("Authorization") auth: String, @Body vehicle: Vehicle.Reservation): Call<Unit>

    @Headers("Content-type: application/json")
    @DELETE("/auth/reservation/{reservationId}")
    fun removeVehicleReservation(@Header("Authorization") auth: String, @Body reservationId: String): Call<Unit>
}