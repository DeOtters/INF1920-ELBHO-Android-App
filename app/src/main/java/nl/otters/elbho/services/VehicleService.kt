package nl.otters.elbho.services

import nl.otters.elbho.models.Vehicle
import retrofit2.Call
import retrofit2.http.*

interface VehicleService {

    @Headers("Content-type: application/json")
    @GET("/auth/reservation")
    fun getAllVehicleReservations(
        @Header("Authorization") auth: String,
        @Query("date") date: String?
    ): Call<ArrayList<Vehicle.CarWithReservations>>

    @Headers("Content-type: application/json")
    @GET("/auth/reservation/me")
    fun getAllVehicleReservationsByAdviser(
        @Header("Authorization") auth: String,
        @Query("after") after: String?,
        @Query("Sort") sort: String?
    ): Call<ArrayList<Vehicle.Reservation>>

    @Headers("Content-type: application/json")
    @POST("/auth/reservation")
    fun createVehicleReservation(
        @Header("Authorization") auth: String,
        @Body vehicleReservation: Vehicle.CreateReservation
    ): Call<Unit>

    @Headers("Content-type: application/json")
    @DELETE("/auth/reservation/{reservationId}")
    fun removeVehicleReservation(
        @Header("Authorization") auth: String,
        @Path("reservationId") reservationId: String
    ): Call<Unit>
}