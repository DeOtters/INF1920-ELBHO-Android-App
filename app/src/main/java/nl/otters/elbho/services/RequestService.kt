package nl.otters.elbho.services

import nl.otters.elbho.models.Request
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.Path

interface RequestService {

    @Headers("Content-type: application/json")
    @GET("requests/{id}")
    fun getRequest(@Header("Authorization") auth: String, @Path("id") id: Int): Call<Request.Properties>

    @Headers("Content-type: application/json")
    @GET("requests/")
    fun getAllRequests(@Header("Authorization") auth: String): Call<ArrayList<Request.Properties>>
}