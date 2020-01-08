package nl.otters.elbho.services

import nl.otters.elbho.models.Invoice
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.*

interface InvoiceService {

    @Headers("Content-type: application/json")
    @GET("auth/invoice/me")
    fun getAllInvoices(@Header("Authorization") auth: String): Call<ArrayList<Invoice.File>>

    @Multipart
    @Headers("Content-type: application/json")
    @POST("/invoices")
    fun createInvoice(
        @Header("Authorization") auth: String,
        @Part("date") date: RequestBody,
        @Part file: MultipartBody.Part
    ): Call<ResponseBody>
}