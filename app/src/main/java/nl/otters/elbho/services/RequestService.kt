package nl.otters.elbho.services

import nl.otters.elbho.models.Invoice
import nl.otters.elbho.models.Request
import retrofit2.Call
import retrofit2.http.*

interface RequestService {

    @Headers("Content-type: application/json")
    @GET("requests/{id}")
    fun getRequest(@Header("x-jwt") auth: String, @Path("id") id: Int): Call<Request.Properties>

    @Headers("Content-type: application/json")
    @GET("requests/")
    fun getAllRequests(@Header("x-jwt") auth: String): Call<ArrayList<Request.Properties>>

    @Headers("Content-type: application/json")
    @POST("/invoices")
    fun createInvoice(@Header("x-jwt") auth: String, @Body invoice: Invoice): Call<Unit>

    @Headers("Content-type: application/json")
    @PUT("/invoices/{id}")
    fun updateInvoice(@Header("x-jwt") auth: String, @Path("id") id: Int, @Body invoice: Invoice): Call<Unit>
}