package nl.otters.elbho.services

import nl.otters.elbho.models.Location
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.PUT

interface LocationService {

    @Headers("Content-type: application/json")
    @PUT("auth/location")
    fun putLocation(
        @Header("Authorization") auth: String,
        @Body location: Location.Properties
    ): Call<Location.Properties>
}