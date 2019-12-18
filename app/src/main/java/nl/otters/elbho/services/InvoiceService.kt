package nl.otters.elbho.services

import nl.otters.elbho.models.Invoice
import retrofit2.Call
import retrofit2.http.*

interface InvoiceService {

    @Headers("Content-type: application/json")
    @GET("advisor/me/invoices")
    fun getAllInvoices(@Header("Authorization") auth: String): Call<ArrayList<Invoice.File>>

    @Headers("Content-type: application/json")
    @GET("/invoices/{id}")
    fun getInvoice(@Header("Authorization") auth: String, @Path("id") id: Int): Call<Invoice.File>

    @Headers("Content-type: application/json")
    @POST("/invoices")
    fun createInvoice(@Header("Authorization") auth: String, @Body invoice: Invoice.File): Call<Unit>

    @Headers("Content-type: application/json")
    @PUT("/invoices/{id}")
    fun updateInvoice(@Header("Authorization") auth: String, @Path("id") id: Int, @Body invoice: Invoice.File): Call<Unit>
}