package nl.otters.elbho.services

import nl.otters.elbho.models.Vehicle
import retrofit2.Call
import retrofit2.http.*

interface VehicleService {
    @Headers("Content-type: application/json")
    @GET("vehicles")
    fun getAllVehicles(@Header( "x-jwt") authToken : String?): Call<ArrayList<Vehicle.Car>>

    @Headers("Content-type: application/json")
    @GET("vehicles/{id}")
    fun getVehicle(@Header( "x-jwt") authToken : String?, @Path("id") vehicleId: String): Call<Vehicle.Car>

    // We should only update the location of the Vehicle
    @Headers("Content-type: application/json")
    @PUT("vehicles/{id}")
    fun updateVehicle(@Header( "x-jwt") authToken : String?, @Path("id") vehicleId: String): Call<Vehicle.Car>

    @Headers("Content-type: application/json")
    @GET("advisors/me/vehicles/claims")
    fun getAllVehiclesClaims(@Header( "x-jwt") authToken : String?): Call<ArrayList<Vehicle.Reservation>>

    @Headers("Content-type: application/json")
    @DELETE("vehicles/claims/{id}")
    fun deleteClaim(@Header( "x-jwt") authToken : String?, @Path("id") claimId: String): Call<Unit>
}