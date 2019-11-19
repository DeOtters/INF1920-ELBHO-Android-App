package nl.otters.elbho.services

import nl.otters.elbho.models.Adviser
import nl.otters.elbho.models.Invoice
import retrofit2.Call
import retrofit2.http.*

interface InvoiceService {

    @Headers("Content-type: application/json")
    @GET("advisor/me/invoices")
    fun getAllInvoices(@Header("x-jwt") auth: Adviser.Authentication): Call<ArrayList<Invoice>>

    @Headers("Content-type: application/json")
    @GET("/invoices/{id}")
    fun getInvoice(@Header("x-jwt") auth: Adviser.Authentication, @Path("id") id: Int): Call<Invoice>

    @Headers("Content-type: application/json")
    @POST("/invoices")
    fun createInvoice(@Header("x-jwt") auth: Adviser.Authentication, @Body invoice: Invoice): Call<Unit>

    @Headers("Content-type: application/json")
    @PUT("/invoices/{id}")
    fun updateInvoice(@Header("x-jwt") auth: Adviser.Authentication, @Path("id") id: Int, @Body invoice: Invoice): Call<Unit>
}