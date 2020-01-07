package nl.otters.elbho.services

import nl.otters.elbho.models.Invoice
import retrofit2.Call
import retrofit2.http.*

interface InvoiceService {

    @Headers("Content-type: application/json")
    @GET("auth/invoice/me")
    fun getAllInvoices(@Header("Authorization") auth: String): Call<ArrayList<Invoice.File>>

    @Headers("Content-type: application/json")
    @POST("/invoices")
    fun createInvoice(@Header("Authorization") auth: String, @Body properties: Invoice.Upload): Call<Unit>
}